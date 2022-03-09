package strategy;

import core.Cell;

import java.util.List;

public class SinglePointStrategy extends SweeperStrategy{
    @Override
    public List<Cell> getNextProbe() {

        for (Cell hiddenCell : knowledgeBase.getHiddenCells()) {
            // for each hidden cell, loop through all its PROBED neighbours and check if any satisfy amn or afn
            for (Cell uncoveredNeighbour : knowledgeBase.getUncoveredNeighbours(hiddenCell)) {
                if (knowledgeBase.getHiddenNeighbours(uncoveredNeighbour).size() > 0) {
                    if (allFreeNeighbours(uncoveredNeighbour)) {
                        setShouldProbeCell(true);
                        return List.of(hiddenCell);
                    } else if (allMarkedNeighbours(uncoveredNeighbour)) {
                        setShouldProbeCell(false);
                        return List.of(hiddenCell);
                    }
                }
            }
        }
        return List.of();
    }

    private boolean allFreeNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getFlaggedNeighbours(cell).size() == clue;
    }


    private boolean allMarkedNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getHiddenNeighbours(cell).size() == (clue - knowledgeBase.getFlaggedNeighbours(cell).size());
    }

    @Override
    public List<Cell> getInitMove() {
        return List.of(
                new Cell(0, 0),
                new Cell(knowledgeBase.getMapHeight() / 2, knowledgeBase.getMapWidth() / 2)
        );
    }
}
