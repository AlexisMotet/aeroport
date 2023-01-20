package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class DechargementPassagers extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Ravitaillement Avion", new Gaussienne(10, 0));
    }};
    public DechargementPassagers(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Dechargement Passagers";
    }

    @Override
    public String toString() {
        return "Dechargement Passagers";
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ATTERI);
        LogicalDuration futureDate = LogicalDuration.ofMinutes(
                attentes.get("Attente Ravitaillement Avion").next());
        LogicalDateTime date = getDateOccurence().add(futureDate);
        avion.getEngine().postEvent(new RavitaillementAvion(getEntity(), date));
    }
}
