package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class DechargementPassagers extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Dechargements Passagers", new Gaussienne(10, 0));
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
                attentes.get("duree Dechargements Passagers").next());
        LogicalDateTime date = getDateOccurence().add(futureDate);
        avion.getEngine().postEvent(new RavitaillementAvion(getEntity(), date));
    }
}
