package core;

import strategy.SweeperStrategy;

import java.util.List;

public class Agent {

    private KnowledgeBase knowledgeBase;
    private final boolean verbose;
    private SweeperStrategy strategy;
    private Game game;

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

    public void traverse() {
        //init move
        initialMove();
        while (!game.isGameOver()) {

            // check if the remaining hidden cells are mines. if so, flag alll of them
            if (allRemainingHiddenCellsAreMines()) {
                knowledgeBase.getHiddenCells().forEach(this::flagCell);
            }

            if (noHiddenCellsAndCorrectFlaggedMines()) {
                // player has probes all the hidden cells and has the correct number of mines . so win game
                winGame();
                break;
            }

            // do probing
            List<Cell> cells = strategy.getNextProbe();
            if (cells.size() != 0) {
                if (strategy.isShouldProbeCell()) {
                    cells.forEach(this::probe);
                } else {
                    cells.forEach(this::flagCell);
                }
            } else {
                // no more logical moves
                gameOverDueToNoLogicalMoves();
                break;
            }

        }
    }

    public boolean allRemainingHiddenCellsAreMines() {
        return knowledgeBase.getHiddenCells().size() == game.getNumberOfMines() - knowledgeBase.getFlaggedCells().size();
    }

    public boolean noHiddenCellsAndCorrectFlaggedMines() {
        return knowledgeBase.getHiddenCells().size() == 0 && knowledgeBase.getFlaggedCells().size() == game.getNumberOfMines();
    }


    public void initialMove() {
        // first thing is to add all the blocked cells to the knowledge base
        game.getBlockedCells().forEach(this::uncoverCell);
        // you can probe the top left cell and the middle cell
        probe(new Cell(0, 0));
        char[][] kbView = knowledgeBase.getMapView();
        int lengthMidPoint = (kbView.length / 2) + 1; // since java floors int division;
        int widthMidPoint = (kbView[0].length / 2) + 1; // since java floors int division;
        probe(new Cell(lengthMidPoint, widthMidPoint));
        probe(new Cell(lengthMidPoint, widthMidPoint));
    }

    private void probe(Cell cell) {
        if (knowledgeBase.getUncoveredCells().contains(cell)) {
            return;
        }
        // probe the cell from the original mpa
        BoardCellType type = getGame().probe(cell);

        if (type == BoardCellType.MINE) {
            gameOverByFoundMine();
            return;
        }
        // update knowledge base
        uncoverCell(cell);

        // Uncover all neighbours if current cell clue is zero.
        if (type == BoardCellType.MINE_NEIGBOUR_0) {
            knowledgeBase.getHiddenNeighbours(cell).forEach(this::uncoverCell);
        }
        // Log board if verbose is true
        if (verbose) {
            printBoard(knowledgeBase.getMapView());
        }

    }

    private void flagCell(Cell cell) {
        knowledgeBase.flagCell(cell);
    }

    private void uncoverCell(Cell cell) {
        // uncover cell from game and set to agent view
        // this is safe probing. i.e only used in situations where probing a mine is impossible
        knowledgeBase.uncoverCell(cell, getGame().probe(cell).getCharValue());
    }

    private void gameOverDueToNoLogicalMoves() {
        printBoard(knowledgeBase.getMapView());
        game.gameOverDueToNoLogicalMoves();
    }

    private void gameOverByFoundMine() {
        printBoard(knowledgeBase.getMapView());
        game.gameOverByFoundMine();
    }

    private void winGame() {
        printBoard(knowledgeBase.getMapView());
        game.winGame();
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

    public void setStrategy(SweeperStrategy strategy) {
        this.strategy = strategy;
    }
}
