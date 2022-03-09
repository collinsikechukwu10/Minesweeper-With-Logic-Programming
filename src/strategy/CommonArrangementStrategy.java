package strategy;

import core.BoardCellType;
import core.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonArrangementStrategy extends SinglePointStrategy {
    private final Set<Integer> oneTwoPatternClueValues = Set.of(1, 2);


    @Override
    public List<Cell> getNextProbe() {
        // ONLY LOOK THROGH UNBLOCKED CELLS
        List<Cell> unblockedUncoveredCells = knowledgeBase.getUncoveredCells().stream()
                .filter(x -> knowledgeBase.getKnowledgeBaseCellTypeAt(x) != BoardCellType.BLOCK)
                .collect(Collectors.toList());
        for (Cell uncoveredCell : unblockedUncoveredCells) {
            List<Cell> probeCells = oneTwoStrategy(uncoveredCell);
            if (!probeCells.isEmpty()) {
                setShouldProbeCell(false);
//                System.out.println("Found One-Two Pattern, flagging: " + probeCells + knowledgeBase.getMapWidth());
                return probeCells;
            }
        }
        return super.getNextProbe();
    }

    private int getEffectiveClue(Cell cell) {
        return knowledgeBase.getKnowledgeBaseCellTypeAt(cell).getIntValue() - knowledgeBase.getFlaggedNeighbours(cell).size();
    }

    private List<Cell> oneTwoStrategy(Cell uncoveredCell) {
        // check each uncovered cell for a one any cell that has one or two, and a vertical or horizontal neighbour that is either 1 or 2

        int uncoveredCellClue = getEffectiveClue(uncoveredCell);

        if (oneTwoPatternClueValues.contains(uncoveredCellClue)) {
            // filter non diagonal neighbours
            List<Cell> nonDiagonalNeighbours = knowledgeBase.getUncoveredNeighbours(uncoveredCell)
                    .stream().filter(x -> x.isNonDiagonalNeighbour(uncoveredCell)).collect(Collectors.toList());
            // check if there is an non diagonal neighbour with either 2 or 1 as well
            for (Cell nonDiagonalUncoveredNeighbour : nonDiagonalNeighbours) {
                // get the resolved cell clue
                int nonDiagonalUncoveredNeighbourClue = getEffectiveClue(nonDiagonalUncoveredNeighbour);
                if (oneTwoPatternClueValues.contains(nonDiagonalUncoveredNeighbourClue)) {
                    Cell cell = null;
                    if (uncoveredCellClue == 1 && nonDiagonalUncoveredNeighbourClue == 2) {
                        cell = resolveOneTwoCell(uncoveredCell, nonDiagonalUncoveredNeighbour);
                    } else if (uncoveredCellClue == 2 && nonDiagonalUncoveredNeighbourClue == 1) {
                        cell = resolveOneTwoCell(nonDiagonalUncoveredNeighbour, uncoveredCell);
                    }
                    if (cell != null) {
                        return List.of(cell);
                    }
                }
            }

        }
        return List.of();
    }

    private Cell resolveOneTwoCell(Cell oneCell, Cell twoCell) {
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

        // make sure that the cells exist in the map
        cellsToFlag = cellsToFlag.stream().filter(knowledgeBase::isOnBoard).collect(Collectors.toList());
        Cell uncoveredCell = cellsToFlag.stream().filter(c -> knowledgeBase.getUncoveredCells().contains(c)).findFirst().orElse(null);
        if (uncoveredCell != null) {
            // this means that the other one is a mine
            cellsToFlag.remove(uncoveredCell);
            if (cellsToFlag.size() != 0 && !knowledgeBase.getFlaggedCells().contains(cellsToFlag.get(0))) {
                return cellsToFlag.get(0);
            }
        }
        return null;
    }

}
