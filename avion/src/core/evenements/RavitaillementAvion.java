package core.evenements;

import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.outils.OutilDate;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class RavitaillementAvion extends EvenementAvion {
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Ravitaillement Avion", new Gaussienne(50, 0.1));
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
        avion.setDateRavitaillement(getDateOccurence());
        LogicalDateTime futureDate = getDateOccurence().add(LogicalDuration.ofMinutes(
                attentes.get("duree Ravitaillement Avion").next()));
        if (!OutilDate.checkSiJour(futureDate))
        {
            avion.getEngine().postEvent(new Embarquement(getEntity(),
                    OutilDate.obtenirProchainMatin(futureDate)));
        }
        else {
            avion.getEngine().postEvent(new Embarquement(getEntity(), futureDate));
        }

    }
}
