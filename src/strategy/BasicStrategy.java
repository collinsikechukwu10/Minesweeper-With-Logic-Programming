package strategy;

import core.Cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicStrategy extends SweeperStrategy {

    @Override
    public List<Cell> getNextProbe() {
        List<Cell> nextProbes = new ArrayList<>();
        if (knowledgeBase.getHiddenCells().size() != 0) {
            Iterator<Cell> it = knowledgeBase.getHiddenCells().iterator();
            nextProbes.add(it.next());
        }
        setShouldProbeCell(true);
        return nextProbes;
    }

}
