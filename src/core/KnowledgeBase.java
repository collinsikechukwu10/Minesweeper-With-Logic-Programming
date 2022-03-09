package core;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeBase {
    private final Set<Cell> flaggedCells;
    private final Set<Cell> uncoveredCells;
    private final Set<Cell> hiddenCells;
    private final char[][] mapView;
    private final int mapHeight;
    private final int mapWidth;

    public KnowledgeBase(int mapHeight, int mapWidth) {
        // linked list is used to maintain order of insertion.
        hiddenCells = new LinkedHashSet<>();
        uncoveredCells = new LinkedHashSet<>();
        flaggedCells = new LinkedHashSet<>();
        this.mapHeight=mapHeight;
        this.mapWidth=mapWidth;
        mapView = new char[this.mapHeight][this.mapWidth];
        for (int i = 0; i < this.mapHeight; i++) {
            for (int j = 0; j < this.mapWidth; j++) {
                mapView[i][j] = BoardCellType.HIDDEN.getCharValue();
                hiddenCells.add(new Cell(i, j));
            }
        }
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
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

    public BoardCellType getKnowledgeBaseCellTypeAt(Cell cell) {
        return BoardCellType.resolve(mapView[cell.getR()][cell.getC()]);
    }

    public char[][] getMapView() {
        return mapView;
    }

    public Set<Cell> getHiddenCells() {
        return hiddenCells;
    }

    public Cell getNextHiddenCell() {
        if (this.getHiddenCells().size() != 0) {
            Iterator<Cell> it = this.getHiddenCells().iterator();
            return it.next();
        }
        return null;
    }

    public Iterator<Cell> getHiddenCellIterator() {
        return this.getHiddenCells().iterator();
    }

    public List<Cell> getNeighbours(Cell cell) {
        // get all 8 neighbours if they exist
        int r = cell.getR();
        int c = cell.getC();
        List<Cell> neighbours = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Cell neighbour = new Cell(r + i, c + j);
                if (!neighbour.equals(cell) && isOnBoard(neighbour)) {
                    neighbours.add(neighbour);
                }
            }
        }
        return neighbours;
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

    public boolean isOnBoard(Cell cell) {
        return cell.getR() >= 0 && cell.getR() < mapView.length && cell.getC() >= 0 && cell.getC() < mapView[0].length;
    }


}
