package strategy;

import core.Cell;

import java.util.List;

public class BasicStrategy extends SweeperStrategy {

    @Override
    public List<Cell> getNextProbe() {
        Cell nextProbe = knowledgeBase.getNextHiddenCell();
        setShouldProbeCell(true);
        return List.of(nextProbe);
    }

    @Override
    public void clean() {
    }

    @Override
    public List<Cell> getInitMove() {
        return List.of(new Cell(0,0));
    }

}
