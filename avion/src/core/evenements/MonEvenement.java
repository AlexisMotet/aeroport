package core.evenements;

import core.Avion;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public abstract class MonEvenement extends SimEvent {
    public MonEvenement(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes()
    {
        throw new IllegalStateException("La methode getAttentes doit etre surchargee");
    }

    public abstract String toString();

}