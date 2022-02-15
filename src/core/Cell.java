package core;


import java.util.Objects;

public class Cell {
    private final int r;
    private final int c;

    public Cell(int r, int c) {
        this.r = r;
        this.c = c;
    }


    public int getC() {
        return c;
    }

    public int getR() {
        return r;
    }


    @Override
    public String toString() {
        return "Cell{" + r + "," + c + '}';
    }

    public boolean isAdjacent(Cell other) {

        return Math.max(Math.abs(getR() - other.getR()), Math.abs(getC() - other.getC())) == 1;
    }

    @Override
    public boolean equals(Object o) {
        Cell cell = (Cell) o;
        return r == cell.getR() && c == cell.getC();
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }
}
