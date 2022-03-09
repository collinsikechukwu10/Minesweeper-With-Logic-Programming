import core.Agent;
import core.Game;
import core.KnowledgeBase;
import core.World;
import eval.GameEvaluator;
import strategy.*;

public class A2main {

    public static void main(String[] args) {

        boolean verbose = args.length > 2 && args[2].equals("verbose");
        boolean eval = args[0].equals("eval");

        if (eval) {
            GameEvaluator evaluator = new GameEvaluator();
            evaluator.evaluate();
        } else {
            System.out.println("-------------------------------------------\n");
            System.out.println("Agent " + args[0] + " plays " + args[1] + "\n");
            SweeperStrategy strategy = resolveStrategy(args[0]);
            World world = World.valueOf(args[1]);
            test(world, strategy, verbose);
        }
    }

    private static void test(World world, SweeperStrategy strategy, boolean verbose) {
        char[][] board = world.getMap();

        Game game = new Game(board);
        KnowledgeBase knowledgeBase = new KnowledgeBase(game.getGameBoardHeight(), game.getGameBoardWidth());
        strategy.setKnowledgeBase(knowledgeBase);
        Agent agent = new Agent(verbose);
        agent.setKnowledgeBase(knowledgeBase);
        agent.setStrategy(strategy);
        agent.setGame(game);


        printBoard(board);
        System.out.println("Start!");
        agent.traverse();
    }

    private static SweeperStrategy resolveStrategy(String strategyString) {
        SweeperStrategy strategy;
        switch (strategyString) {
            case "P1":
            default:
                //TODO: Part 1
                strategy = new BasicStrategy();
                break;
            case "P2":
                //TODO: Part 2
                strategy = new SinglePointStrategy();
                break;
            case "P3":
                //TODO: Part 3
                strategy = new DnfSatisfiabilityTestReasoningStrategy();
                break;
            case "P4":
//                //TODO: Part 4
                strategy = new CnfSatisfiabilityTestReasoningStrategy();
                break;
            case "P5":
//                //TODO: Part 5
                strategy = new CommonArrangementStrategy();
                break;
        }
        return strategy;
    }

    //prints the board in the required format - PLEASE DO NOT MODIFY
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


}
