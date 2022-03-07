package strategy;

import core.Cell;
import core.LogicUtils;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.ArrayList;
import java.util.List;

public class CnfSatisfiabilityTestReasoningStrategy extends DnfSatisfiabilityTestReasoningStrategy {
    private final int MAXVAR = 1000000;
    private final int NBCLAUSES = 500000;

    public String atMost(List<Cell> neighbours, int clue) {

        List<int[]> combinations = LogicUtils.combinator(neighbours.size(), clue + 1);
        List<String> clauses = new ArrayList<>();
        combinations.forEach(combination -> {
            List<String> literals = new ArrayList<>();
            for (int i = 0; i < combination.length; i++) {
                literals.add(LogicUtils.not(LogicUtils.toLiteral(neighbours.get(i))));
            }
            clauses.add(LogicUtils.orAll(literals));
        });
        return LogicUtils.andAll(clauses);

    }


    public String atMostNot(List<Cell> neighbours, int clue) {
        List<int[]> combinations = LogicUtils.combinator(neighbours.size(), clue + 1);
        List<String> clauses = new ArrayList<>();
        combinations.forEach(combination -> {
            List<String> literals = new ArrayList<>();
            for (int i = 0; i < combination.length; i++) {
                literals.add(LogicUtils.not(LogicUtils.toLiteral(neighbours.get(i))));
            }
            clauses.add(LogicUtils.orAll(literals));
        });
        return LogicUtils.andAll(clauses);
    }


    public String atExactly(List<Cell> cells, int clue) {
        List<String> mostClauses = new ArrayList<>();
        mostClauses.add(atMost(cells, clue));
        mostClauses.add(atMostNot(cells, cells.size() - clue));
        return LogicUtils.andAll(mostClauses);
    }


    public List<Cell> solve(Cell cell) throws TimeoutException, ContradictionException {

        ISolver solver = SolverFactory.newDefault();
        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        // Feed the solver using Dimacs format, using arrays of int
        // (best option to avoid dependencies on SAT4J IVecInt)
        for (int i = 0; i < NBCLAUSES; i++) {
            int[] clause = null;// get the clause from somewhere
            // the clause should not contain a 0, only integer (positive or negative)
            // with absolute values less or equal to MAXVAR
            // e.g. int [] clause = {1, -3, 7}; is fine, while int [] clause = {1, -3, 7, 0}; is not fine
            solver.addClause(new VecInt(clause)); // adapt Array to IVecInt

        }
        // we are done. Working now on the IProblem interface
        IProblem problem = solver;
        setShouldProbeCell(problem.isSatisfiable());
        return List.of(cell);
    }

    public int cellToClauseId(Cell cell) {
        return (knowledgeBase.getMapWidth() * cell.getR()) + (1 + cell.getC());
    }


}
