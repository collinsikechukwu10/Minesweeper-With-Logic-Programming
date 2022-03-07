package eval;

import core.*;
import strategy.BasicStrategy;
import strategy.SweeperStrategy;
import strategy.CnfSatisfiabilityTestReasoningStrategy;
import strategy.SinglePointStrategy;
import strategy.DnfSatisfiabilityTestReasoningStrategy;

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

        Result(SweeperStrategy strategy, World world, GameStatus status) {
            this.status = status;
            this.world = world;
            this.strategy = strategy;
        }

        String toCSVLine() {
            List<String> csvLine = new ArrayList<>();
            csvLine.add(world.name());
            csvLine.add(world.shape());
            csvLine.add(strategy.getClass().getSimpleName());
            csvLine.add(status.getMessage());
            return String.join(",", csvLine);
        }

        static String generateReport(List<Result> resultList){
            return resultList.stream().map(Result::toCSVLine).collect(Collectors.joining("\n"));
        }
    }

    private final List<? extends SweeperStrategy> gameStrategies = List.of(
            new BasicStrategy(),
            new CnfSatisfiabilityTestReasoningStrategy(),
            new SinglePointStrategy(),
            new DnfSatisfiabilityTestReasoningStrategy());


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
        return new Result(strategy, world, game.getStatus());
    }
}
