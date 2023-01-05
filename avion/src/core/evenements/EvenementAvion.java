package core.evenements;

import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public abstract class EvenementAvion extends SimEvent {

    public EvenementAvion(SimEntity entity, LogicalDateTime dateOccurence) {
        super(entity, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes()
    {
        throw new IllegalStateException("La methode getAttentes doit etre surchargee");
    }

    @Override
    public abstract String toString();
}
