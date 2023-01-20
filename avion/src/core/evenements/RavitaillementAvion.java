package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class RavitaillementAvion extends EvenementAvion {
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Embarquement", new Gaussienne(50, 0.1));
    }};
    public RavitaillementAvion(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Ravitaillement Avion";
    }

    @Override
    public String toString() {
        return "Ravitaillement Avion";
    }

    @Override
    public void process() {
        LogicalDuration futureDate = LogicalDuration.ofMinutes(
                attentes.get("Attente Embarquement").next());
        LogicalDateTime date = getDateOccurence().add(futureDate);
        avion.getEngine().postEvent(new Embarquement(getEntity(), date));
    }
}
