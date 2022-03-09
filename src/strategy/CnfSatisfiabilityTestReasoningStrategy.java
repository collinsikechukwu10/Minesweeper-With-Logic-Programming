package strategy;

import core.BoardCellType;
import core.Cell;
import core.LogicUtils;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.*;

public class CnfSatisfiabilityTestReasoningStrategy extends SinglePointStrategy {
    private final int MAXVAR = 1000000;

    @Override
    public List<Cell> getNextProbe() {
        // get next probe
        Iterator<Cell> cellIterator = knowledgeBase.getHiddenCellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = solve(cellIterator.next());
            if (cell != null) {
                return List.of(cell);
            }
        }
        // dnf could not resolve any cell. fallback try sps
        return super.getNextProbe();
    }


    public Cell solve(Cell cell){
        Set<String> kbu = fillKBU();
        // add current cell
        kbu.add(LogicUtils.toLiteral(cell));
        // convert to dimacs format
        List<int[]> dimacsClauses = convertCnfToDimacs(kbu);
        ISolver solver = SolverFactory.newDefault();
        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(dimacsClauses.size());
        for (int[] dimacsClause : dimacsClauses) {
            try {
                solver.addClause(new VecInt(dimacsClause));
            } catch (ContradictionException e) {
//                e.printStackTrace();
            }
        }
        IProblem problem = solver;
        try {
            if (!problem.isSatisfiable()) {
                setShouldProbeCell(true);
                return cell;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<int[]> convertCnfToDimacs(Set<String> clauses) {
        List<int[]> dimacsClauses = new ArrayList<>();
        for (String clause : clauses) {
            // split by disjunction
            List<Integer> literals = new ArrayList<>();
            for (String literal : clause.split("\\" + LogicUtils.DISJUNCTION_SYMBOL)) {
                int multiplier = literal.contains(LogicUtils.NEGATION_SYMBOL) ? -1 : 1;
                Cell cell = LogicUtils.toCell(literal.replace(LogicUtils.NEGATION_SYMBOL, ""));
                literals.add(cellToClauseId(cell) * multiplier);
            }
            dimacsClauses.add(literals.stream().mapToInt(i -> i).toArray());
        }
        return dimacsClauses;
    }

    public Set<String> fillKBU() {
        Set<String> uncoveredCellKBU = new HashSet<>();
        // add all uncovered cells as well as their hidden neighbour information
        knowledgeBase.getUncoveredCells().forEach(cell -> {
            BoardCellType type = knowledgeBase.getKnowledgeBaseCellTypeAt(cell);
            // check if a cell is not a block or if all its neighbours have not been discovered
            if (type != BoardCellType.BLOCK && knowledgeBase.getHiddenNeighbours(cell).size() > 0) {
                int clue = type.getIntValue();
                // take out the number of flags that we have already found from the clue
                clue = clue - knowledgeBase.getFlaggedNeighbours(cell).size();
                List<Cell> cellHiddenNeighbours = knowledgeBase.getHiddenNeighbours(cell);
                uncoveredCellKBU.addAll(atExactly(cellHiddenNeighbours, clue));
            }
        });
        return uncoveredCellKBU;
    }

    public List<String> atMost(List<Cell> neighbours, int clue, boolean isMine) {
        List<int[]> combinations = LogicUtils.combinator(neighbours.size(), clue + 1);
        List<String> clauses = new ArrayList<>();
        combinations.forEach(combination -> {
            List<String> literals = new ArrayList<>();
            for (int i = 0; i < combination.length; i++) {
                String literal = LogicUtils.toLiteral(neighbours.get(combination[i]));
                if (isMine) {
                    literal = LogicUtils.not(literal);
                }
                literals.add(literal);
            }
            clauses.add(String.join(LogicUtils.DISJUNCTION_SYMBOL, literals));
        });
        return clauses;
    }


    public List<String> atExactly(List<Cell> cells, int clue) {
        List<String> atExactlyClauses = new ArrayList<>();
        atExactlyClauses.addAll(atMost(cells, clue, true));
        atExactlyClauses.addAll(atMost(cells, cells.size() - clue, false));
        return atExactlyClauses;
    }


    public int cellToClauseId(Cell cell) {
        return (knowledgeBase.getMapWidth() * cell.getR()) + (1 + cell.getC());
    }
}
