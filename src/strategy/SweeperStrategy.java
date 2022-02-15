package strategy;

import core.Cell;
import core.KnowledgeBase;

import java.util.List;

public abstract class SweeperStrategy {
    KnowledgeBase knowledgeBase=null;
    private boolean shouldProbeCell=false;

    public SweeperStrategy(){
    }

    public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public abstract List<Cell> getNextProbe();


    public boolean isShouldProbeCell() {
        return shouldProbeCell;
    }

    public void setShouldProbeCell(boolean shouldProbeCell) {
        this.shouldProbeCell = shouldProbeCell;
    }
}
