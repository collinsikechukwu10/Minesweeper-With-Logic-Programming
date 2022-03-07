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

    public GameStatus getStatus() {
        return status;
    }

    public List<Cell> initialCellsToProbe() {
        List<Cell> initCells = new ArrayList<>();
        initCells.add(new Cell(0, 0));
        initCells.add(new Cell((getGameBoardHeight() - 1) / 2, (getGameBoardWidth() - 1) / 2));
        return initCells;
    }

    public boolean hasWon(KnowledgeBase kb) {
        // this condition is only condusive for P1, updarte it
        // for p1, this works
        boolean isWon = (gameBoardWidth * gameBoardHeight) - (kb.getUncoveredCells().size() + countMines()) == 0;
        // however for p2, only should just make sure that the number of hidden cells list is empty.
        // the game would not stall tho as there is a condition in agent that breaks out of the loop if a strategy
        // cannot provide a cell to probe
//        boolean isWon = kb.getHiddenCells().size() == 0;
        if (isWon && status != GameStatus.FOUND_MINE) {
            status = GameStatus.WON;
        }
        return isWon;
    }

    public boolean isStillPlaying(KnowledgeBase kb) {

        return !hasWon(kb) && status == GameStatus.RUNNING;
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

    public void gameOverDueToNoLogicalMoves() {
        status = GameStatus.NO_LOGICAL_MOVES;
    }

    public void endGame() {
        printResult();
    }

    public void printResult() {
        System.out.println(status.getMessage());
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


    public int getGameBoardWidth() {
        return gameBoardWidth;
    }

    public int getGameBoardHeight() {
        return gameBoardHeight;
    }
}
