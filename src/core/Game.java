package core;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final char[][] map;
    private final int gameBoardWidth;
    private final int gameBoardHeight;
    private final int numberOfMines;
    private GameStatus status;


    public Game(char[][] map) {
        this.map = map;
        numberOfMines = countMines();
        status = GameStatus.RUNNING;
        gameBoardHeight = map.length;
        gameBoardWidth = map[0].length;

    }

    private int countMines() {
        int count = 0;
        for (char[] rows : map) {
            for (char type : rows) {
                if (BoardCellType.resolve(type) == BoardCellType.MINE) {
                    count++;
                }
            }
        }
        return count;
    }


    public BoardCellType probe(Cell cell) {
        BoardCellType cellType = BoardCellType.resolve(map[cell.getR()][cell.getC()]);
        if (cellType == BoardCellType.MINE) {
            status = GameStatus.FOUND_MINE;
        }
        return cellType;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public boolean isGameOver() {
        return status != GameStatus.RUNNING;
    }

    public void gameOverDueToNoLogicalMoves() {
        status = GameStatus.NO_LOGICAL_MOVES;
        printResult();
    }

    public void winGame() {
        status = GameStatus.WON;
        printResult();
    }

    public void gameOverByFoundMine() {
        status = GameStatus.FOUND_MINE;
        printResult();
    }

    public List<Cell> getBlockedCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoardCellType cellType = BoardCellType.resolve(map[i][j]);
                if (cellType == BoardCellType.BLOCK) {
                    cells.add(new Cell(i, j));
                }
            }
        }
        return cells;
    }

    public void printResult() {
        System.out.println(status.getMessage());
    }

    public int getGameBoardWidth() {
        return gameBoardWidth;
    }

    public int getGameBoardHeight() {
        return gameBoardHeight;
    }
}
