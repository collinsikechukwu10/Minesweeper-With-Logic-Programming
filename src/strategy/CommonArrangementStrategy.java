package strategy;

import core.Cell;

import java.util.ArrayList;
import java.util.List;

public class CommonArrangementStrategy extends SinglePointStrategy {

    @Override
    public List<Cell> getNextProbe() {
        return super.getNextProbe();
    }

    private List<Cell> oneOneStrategy() {

    }

    private List<Cell> oneTwoStrategy() {
        // check each uncovered cell for a one any cell that has one or two, and a vertical or horizontal neighbour that is either 1 or 2
        for (Cell uncoveredCell : knowledgeBase.getUncoveredCells()) {
            int uncoveredCellClue = knowledgeBase.getKnowledgeBaseCellTypeAt(uncoveredCell).getIntValue();
            uncoveredCellClue -= knowledgeBase.getFlaggedNeighbours(uncoveredCell).size();

            if (uncoveredCellClue == 1 || uncoveredCellClue == 2) {
                // check if there is an non diagonal neighbour with either 2 or 1 as well
                for (Cell uncoveredNeighbour : knowledgeBase.getUncoveredNeighbours(uncoveredCell)) {
                    if (uncoveredNeighbour.isNonDiagonalNeighbour(uncoveredCell)) {
                        // get the resolved cell clue
                        int uncoveredNeighbourClue = knowledgeBase.getKnowledgeBaseCellTypeAt(uncoveredNeighbour).getIntValue();
                        uncoveredNeighbourClue -= knowledgeBase.getFlaggedNeighbours(uncoveredNeighbour).size();
                        Cell cell = null;
                        if (uncoveredCellClue == 1 && uncoveredNeighbourClue == 2) {
                            cell = resolveOneTwoCell(uncoveredCell, uncoveredNeighbour);
                        } else if (uncoveredCellClue == 2 && uncoveredNeighbourClue == 1) {
                            cell = resolveOneTwoCell(uncoveredNeighbour, uncoveredCell);

                        }

                        if (cell != null){
                            return List.of(cell);
                        }
                    }
                }
            }

        }
    }

    private void resolveOneTwoCell(Cell oneCell, Cell twoCell) {
        List<Cell> cellsToFlag = new ArrayList<>();
        int cDiff = (twoCell.getC() - oneCell.getC());
        int rDiff = (twoCell.getR() - oneCell.getR());
        if (cDiff == 0) {
            // that means the are on the same column, we vary cDiff, and move row by twice rDiff
            cellsToFlag.add(new Cell(oneCell.getR() + (2 * rDiff), oneCell.getC() - 1));
            cellsToFlag.add(new Cell(oneCell.getR() + (2 * rDiff), oneCell.getC() + 1));
            // check if one is open

        } else {
            // that means the are on the same row, we vary rDiff, and move row by twice cDiff
            cellsToFlag.add(new Cell(oneCell.getR() + 1, oneCell.getC() + (2 * cDiff)));
            cellsToFlag.add(new Cell(oneCell.getR() - 1, oneCell.getC() + (2 * cDiff)));
            // check if one is open
        }


    }

}
