package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class RoulementArrivee extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Roulement Arrivee", new Gaussienne(4, 2));
    }};
    public RoulementArrivee(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Roulement Arrivee";
    }

    @Override
    public String toString() {
        return "Roulement Arrivee";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.ROULEMENT_ARRIVEE);
        LogicalDateTime futureDate = getDateOccurence().add(
                LogicalDuration.ofMinutes(
                        attentes.get("duree Roulement Arrivee").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), futureDate));
    }
}
