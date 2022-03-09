package eval;

import core.*;
import strategy.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Game Evaluator class.
 * This class is used to evaluate all the strategies implemented.
 *
 * @author 210032207
 * @version 1.0.0
 * @since 30-02-2022
 */
public class GameEvaluator {

    private static class Result {
        private final GameStatus status;
        private final World world;
        private final SweeperStrategy strategy;
        private final float percentageHiddenCells;

        Result(SweeperStrategy strategy, World world, GameStatus status, float percentageHiddenCells) {
            this.status = status;
            this.world = world;
            this.strategy = strategy;
            this.percentageHiddenCells = percentageHiddenCells;
        }

        String toCSVLine() {
            List<String> csvLine = new ArrayList<>();
            csvLine.add(world.name());
            csvLine.add(world.shape());
            csvLine.add(strategy.getClass().getSimpleName());
            csvLine.add(String.valueOf(percentageHiddenCells));
            csvLine.add(status.getMessage().replace("\n", ""));
            return String.join(",", csvLine);
        }

        static String generateReport(List<Result> resultList) {
            String report = "world,world size,strategy,percentageHiddenCells,result\n";
            report += resultList.stream().map(Result::toCSVLine).collect(Collectors.joining("\n"));
            return report;
        }
    }

    private final List<? extends SweeperStrategy> gameStrategies = List.of(
            new BasicStrategy(),
            new CnfSatisfiabilityTestReasoningStrategy(),
            new SinglePointStrategy(),
            new DnfSatisfiabilityTestReasoningStrategy(),//);
            new CommonArrangementStrategy());


    /**
     * Evaluates all the strategies based on the provided configurations
     */
    public void evaluate() {
        // evaluate all provided configurations
        List<Result> results = new ArrayList<>();
        for (World world : World.values()) {
            for (SweeperStrategy gameStrategy : gameStrategies) {
                Result result = evaluateGame(world, gameStrategy);
                results.add(result);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("eval.csv", false));
            writer.append(Result.generateReport(results));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Evaluates all the strategies based on the randomly generated configurations
     */
    public Result evaluateGame(World world, SweeperStrategy strategy) {
        Game game = new Game(world.getMap());
        KnowledgeBase knowledgeBase = new KnowledgeBase(game.getGameBoardHeight(), game.getGameBoardWidth());
        strategy.setKnowledgeBase(knowledgeBase);
        Agent agent = new Agent(false);
        agent.setKnowledgeBase(knowledgeBase);
        agent.setStrategy(strategy);
        agent.setGame(game);
        agent.traverse();
        System.out.println("Agent traversing "+ world.name() + "using " + strategy.getClass().getSimpleName());
        return new Result(strategy, world, game.getStatus(), agent.getPercentageHiddenCells());
    }
}
