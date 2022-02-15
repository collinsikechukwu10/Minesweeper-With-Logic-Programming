package strategy;

import core.Cell;

import java.util.ArrayList;
import java.util.List;

public class SinglePointStrategy extends BasicStrategy {
    @Override
    public List<Cell> getNextProbe() {
        List<Cell> cells = new ArrayList<>();
        // get current covered cell using the basic strategy
        List<Cell> preProbeCells = super.getNextProbe();
        Cell currentCell = preProbeCells.get(0);
        // get all  uncovered neighbours of the current cell
        List<Cell> uncoveredNeighbours = knowledgeBase.getUncoveredNeighbours(currentCell);
        for (Cell neighbour : uncoveredNeighbours) {
            if (allFreeNeighbours(neighbour)) {
                cells.add(currentCell);
                setShouldProbeCell(true);
                break;
            } else if (allMarkedNeighbours(neighbour)) {
                cells.add(currentCell);
                setShouldProbeCell(false);
                break;
            }
        }
        return cells;
    }

    public boolean allFreeNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getFlaggedNeighbours(cell).size() == clue;
    }


    public boolean allMarkedNeighbours(Cell cell) {
        int clue = knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue();
        return knowledgeBase.getHiddenNeighbours(cell).size() == (clue - knowledgeBase.getFlaggedNeighbours(cell).size());
    }
}
