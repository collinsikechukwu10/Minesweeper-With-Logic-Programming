package core;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeBase {
    private final Set<Cell> flaggedCells;
    private final Set<Cell> uncoveredCells;
    private final Set<Cell> hiddenCells;
    private final char[][] mapView;

    public KnowledgeBase(int mapHeight, int mapWidth) {
        hiddenCells = new HashSet<>();
        uncoveredCells = new HashSet<>();
        flaggedCells = new HashSet<>();
        mapView = new char[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                mapView[i][j] = BoardCellType.HIDDEN.getCharValue();
                hiddenCells.add(new Cell(i, j));
            }
        }
    }

    public void flagCell(Cell cell) {
        // remove cell from hidden and add it to flagged
        if (hiddenCells.contains(cell)) {
            mapView[cell.getR()][cell.getC()] = '*';
            hiddenCells.remove(cell);
            flaggedCells.add(cell);
        }

    }

    public Set<Cell> getUncoveredCells() {
        return uncoveredCells;
    }

    public void uncoverCell(Cell cell, char value) {
        if (hiddenCells.contains(cell)) {
            mapView[cell.getR()][cell.getC()] = value;
            hiddenCells.remove(cell);
            uncoveredCells.add(cell);
        }

    }

    public void resetKnowledgeBase() {
        hiddenCells.addAll(uncoveredCells);
        uncoveredCells.clear();

        hiddenCells.addAll(flaggedCells);
        flaggedCells.clear();
    }


    public BoardCellType getKnowledgeBaseCellTypeAt(Cell cell) {
        return BoardCellType.resolve(mapView[cell.getR()][cell.getC()]);
    }

    public char[][] getMapView() {
        return mapView;
    }

    public Set<Cell> getHiddenCells() {
        return hiddenCells;
    }

    public List<Cell> getNeighbours(Cell cell) {
        // get all 8 neighbours if they exist
        int r = cell.getR();
        int c = cell.getC();
        List<Cell> neighbours = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i != 0 && j != 0) {
                    neighbours.add(new Cell(r + i, c + j));
                }
            }
        }
        // only get cell that exist  in the map
        return neighbours.stream().filter(this::isOnBoard).collect(Collectors.toList());
    }

    public List<Cell> getHiddenNeighbours(Cell cell) {
        return getNeighbours(cell).stream().filter(hiddenCells::contains).collect(Collectors.toList());
    }

    public List<Cell> getFlaggedNeighbours(Cell cell) {
        return getNeighbours(cell).stream().filter(flaggedCells::contains).collect(Collectors.toList());
    }

    public List<Cell> getUncoveredNeighbours(Cell cell) {
        return getNeighbours(cell).stream().filter(uncoveredCells::contains).collect(Collectors.toList());
    }

    public Set<Cell> getFlaggedCells() {
        return flaggedCells;
    }

    boolean isOnBoard(Cell cell) {
        return cell.getR() >= 0 && cell.getR() < mapView.length && cell.getC() >= 0 && cell.getC() < mapView[0].length;
    }

}
