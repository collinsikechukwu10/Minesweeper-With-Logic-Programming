package core;

import strategy.SweeperStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Agent {

    private KnowledgeBase knowledgeBase;
    private final boolean verbose;
    private SweeperStrategy strategy;
    private Game game;
    private boolean madeInitMove = false;

    public Agent(boolean verbose) {
        this.verbose = verbose;
    }

    public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public Game getGame() {
        return game;
    }

    private void initialize() {
        // first thing is to add all the blocked cells to the knowledge base
        game.getBlockedCells().forEach(this::uncoverCell);
        if (verbose) {
            logKnowledgeBaseView();
        }
    }

    public void traverse() {
        initialize();
        while (true) {
            // do probing
            List<Cell> cellsToProbe = getNextProbe();

            if (!cellsToProbe.isEmpty()) {
                cellsToProbe.forEach(cell -> {
                    if (game.initialCellsToProbe().contains(cell) || strategy.isShouldProbeCell()) {
                        probe(cell);
                    } else {
                        flagCell(cell);
                    }
                });
            } else {
                // no more logical moves
                game.gameOverDueToNoLogicalMoves();
                break;
            }

            // this condition is only condusive for P1, updarte it
            if (!game.isStillPlaying(knowledgeBase)) {
                break;
            } else {
                if (verbose) {
                    logKnowledgeBaseView();
                }
            }
        }
        endGame();
    }


    private void probe(Cell cell) {
        if (knowledgeBase.getUncoveredCells().contains(cell)) {
            return;
        }
        BoardCellType type = getGame().probe(cell);
        if (type == BoardCellType.MINE) {
            knowledgeBase.uncoverCell(cell, '-');
        } else {
            uncoverCell(cell);
        }


    }

    private List<Cell> getNextProbe() {
        if (madeInitMove) {
            return strategy.getNextProbe();
        } else {
            madeInitMove = true;
            return strategy.getInitMove();
        }
    }

    private void flagCell(Cell cell) {
        knowledgeBase.flagCell(cell);
        // Log board if verbose is true
        if (verbose) {
            logKnowledgeBaseView();
        }
    }

    private void uncoverCell(Cell cell) {
        BoardCellType type = getGame().probe(cell);
        knowledgeBase.uncoverCell(cell, type.getCharValue());
        // Uncover all neighbours if current cell clue is zero.
        if (type == BoardCellType.MINE_NEIGBOUR_0) {
            knowledgeBase.getHiddenNeighbours(cell).forEach(this::uncoverCell);
        }

    }


    private void endGame() {
        System.out.println("Final map");
        logKnowledgeBaseView();
        game.endGame();
    }


    public static void printBoard(char[][] board) {
        System.out.println();
        // first line
        System.out.print("    ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print(j + " "); // x indexes
        }
        System.out.println();
        // second line
        System.out.print("    ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print("- ");// separator
        }
        System.out.println();
        // the board
        for (int i = 0; i < board.length; i++) {
            System.out.print(" " + i + "| ");// index+separator
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");// value in the board
            }
            System.out.println();
        }
        System.out.println();
    }

    private void logKnowledgeBaseView() {
        printBoard(knowledgeBase.getMapView());
    }

    public void setStrategy(SweeperStrategy strategy) {
        this.strategy = strategy;
    }
}
