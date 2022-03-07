package strategy;

import core.BoardCellType;
import core.Cell;
import core.LogicUtils;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.*;
import java.util.stream.Collectors;

public class DnfSatisfiabilityTestReasoningStrategy extends SinglePointStrategy {

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
        // dnf could not resolve any cell. maybe try sps
        return super.getNextProbe();
    }


    private Set<String> fillKBU() {
        Set<String> sentences = new HashSet<>();
        // add all flagged cells to the knowledge base
        knowledgeBase.getFlaggedCells().forEach(cell -> sentences.add(LogicUtils.toLiteral(cell)));
        List<String> uncoveredCellKBU = new ArrayList<>();
        // add all uncovered cells as well as their hidden neighbour information
        knowledgeBase.getUncoveredCells().forEach(cell -> {
            // since the cell is discovered, we can add as not a mine
            sentences.add(LogicUtils.not(LogicUtils.toLiteral(cell)));
            // blocks are not included in the uncovered cells
            BoardCellType type = knowledgeBase.getKnowledgeBaseCellTypeAt(cell);
            // check if a cell is a block or if all its neighbours have been discovered
//            if (type == BoardCellType.BLOCK) sentences.add(LogicUtils.toLiteral(cell));
            if (type != BoardCellType.BLOCK && knowledgeBase.getHiddenNeighbours(cell).size() > 0) {
                int clue = type.getIntValue();
                // take out the number of flags that we have already found from the clue
                clue  = clue - knowledgeBase.getFlaggedNeighbours(cell).size();

                List<Cell> currentCellNeighbours = knowledgeBase.getHiddenNeighbours(cell);
                // use clue and neighbour size to all combinations where only clue numer of mines exist within the neighbours
                // combinator returns a list of all combinations of ids of neighbours that coule be a mine
                List<int[]> combinations = LogicUtils.combinator(currentCellNeighbours.size(), clue);
                List<String> clauses = new ArrayList<>();
                combinations.forEach(combination->{
                    // create a list of unmodified cell string literals
                    String[] arr = currentCellNeighbours.stream().map(LogicUtils::toLiteral).toArray(String[]::new);
                    List<Integer> combs = Arrays.stream(combination).boxed().collect(Collectors.toList());
                    // convert only cells with id that are not in the combinations to "NOT a mine"
                    for (int i = 0; i < arr.length; i++) {
                        if(!combs.contains(i)){
                            arr[i]  = LogicUtils.not(arr[i]);
                        }
                    }
                    clauses.add(LogicUtils.andAll(Arrays.stream(arr).collect(Collectors.toList())));
                });
                uncoveredCellKBU.add(LogicUtils.orAll(clauses));
            }
        });
        // add all the knowledge for each uncovered cell to the main KBU
        if (!uncoveredCellKBU.isEmpty()) {
            sentences.add(LogicUtils.andAll(uncoveredCellKBU));
        }
        return sentences;
    }



    private Cell solve(Cell cell) {

        // get current cell
        Set<String> kbu = fillKBU();
        // add current cell, to check if the cell is a mine
        kbu.add(LogicUtils.not(LogicUtils.toLiteral(cell)));
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        SATSolver miniSat = MiniSat.miniSat(f);
        for (String sentence : kbu) {
            try {
                Formula formula = p.parse(sentence);
                miniSat.add(formula);
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

        // get satisfiability
        Tristate result = miniSat.sat();
        if (result == Tristate.UNDEF) {
            return null;
        }
        setShouldProbeCell(result == Tristate.TRUE);
        return cell;
    }

}
