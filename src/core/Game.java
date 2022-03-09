package core;

import strategy.BasicStrategy;
import strategy.SweeperStrategy;

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

    public boolean hasWon(SweeperStrategy strategy) {
        boolean isWon;
        if (strategy instanceof BasicStrategy) {
            // for p1, this works
            isWon = (gameBoardWidth * gameBoardHeight) - (strategy.getKnowledgeBase().getUncoveredCells().size() + countMines()) == 0;

        } else {
            // however for p2, only should just make sure that the number of hidden cells list is empty.
            // the game would not stall tho as there is a condition in agent that breaks out of the loop if a strategy
            // cannot provide a cell to probe
            isWon = strategy.getKnowledgeBase().getHiddenCells().size() == 0;
        }
        if (isWon && status != GameStatus.FOUND_MINE) {
            status = GameStatus.WON;
        }
        return isWon;
    }

    public boolean isStillPlaying(SweeperStrategy strategy) {

        return !hasWon(strategy) && status == GameStatus.RUNNING;
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


    public void gameOverDueToNoLogicalMoves() {
        status = GameStatus.NO_LOGICAL_MOVES;
    }

    public void endGame() {
        printResult();
    }

    public void printResult() {
        System.out.println("\n" + status.getMessage());
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
