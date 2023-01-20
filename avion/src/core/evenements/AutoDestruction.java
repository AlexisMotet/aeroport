package core.evenements;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;

public class AutoDestruction extends EvenementAvion {
    public AutoDestruction(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }
    @Override
    public void process() {
        avion.getEngine().getEntityList().remove(avion);
    }

    @Override
    public String toString() {
        return "AutoDestruction";
    }
}
