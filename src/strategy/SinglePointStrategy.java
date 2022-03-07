package strategy;

import core.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SinglePointStrategy extends BasicStrategy {
    @Override
    public List<Cell> getNextProbe() {
        List<Cell> cellsToCheck = new ArrayList<>();
        // TODO, sort out how the first cell  (0,0) gets started,
        //  may do that from the traverse function in the agent class

        for (Cell hiddenCell : knowledgeBase.getHiddenCells()) {
            // for each hidden cell, loop through all its PROBED neighbours and check if any satisfy amn or afn
            for (Cell uncoveredNeighbour : knowledgeBase.getUncoveredNeighbours(hiddenCell)) {
                if (knowledgeBase.getHiddenNeighbours(uncoveredNeighbour).size() > 0) {
                    if (allFreeNeighbours(uncoveredNeighbour)) {
                        setShouldProbeCell(true);
                        return knowledgeBase.getHiddenNeighbours(uncoveredNeighbour);
                    } else if (allMarkedNeighbours(uncoveredNeighbour)) {
                        setShouldProbeCell(false);
                        return knowledgeBase.getHiddenNeighbours(uncoveredNeighbour);
                    }
                }
            }
        }
        // get current covered cell using the basic strategy
//
//        for (Cell uncoveredCell : knowledgeBase.getUncoveredCells()) {
//            // get a list of uncovered cells that have at least one hidden neighbour
//            if(knowledgeBase.getHiddenNeighbours(uncoveredCell).size()>0){
//                if (allFreeNeighbours(uncoveredCell)){
//                    setShouldProbeCell(true);
//                    cellsToCheck.addAll(knowledgeBase.getHiddenNeighbours(uncoveredCell));
//                    break;
//                }else if (allMarkedNeighbours(uncoveredCell)) {
//                    setShouldProbeCell(false);
//                    cellsToCheck.addAll(knowledgeBase.getHiddenNeighbours(uncoveredCell));
//                    break;
//                }
//            }
//        }
        return cellsToCheck;
    }

    public boolean allFreeNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getFlaggedNeighbours(cell).size() == clue;
    }


    public boolean allMarkedNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getHiddenNeighbours(cell).size() == (clue - knowledgeBase.getFlaggedNeighbours(cell).size());
    }

    @Override
    public void clean() {
        // only used when game has ended, if hidden cells still exist that means that they are all mines
//        List<Cell> cellsToFlag = new ArrayList<>(knowledgeBase.getHiddenCells());
//        if(cellsToFlag.size()>0){
//            cellsToFlag.forEach(knowledgeBase::flagCell);
//        }
    }

    @Override
    public List<Cell> getInitMove() {
        return List.of(
                new Cell(0, 0),
                new Cell(knowledgeBase.getMapHeight() / 2, knowledgeBase.getMapWidth() / 2)
        );
    }
}
