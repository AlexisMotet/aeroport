package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class RoulementDepart extends EvenementAvion {
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Roulement Depart", new Gaussienne(4, 2));
    }};
    public RoulementDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static String getNom(){
        return "Roulement Depart";
    }

    @Override
    public String toString() {
        return "Roulement Depart";
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ROULEMENT_DEPART);
        LogicalDateTime futureDate = getDateOccurence().add(
                LogicalDuration.ofMinutes(attentes.get("duree Roulement Depart").next()));
        avion.getEngine().postEvent(new Decollage(getEntity(), futureDate));
    }
}
