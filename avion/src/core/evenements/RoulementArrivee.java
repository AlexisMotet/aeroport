package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class RoulementArrivee extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Fin De Vol", new Gaussienne(4, 2));
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
                        attentes.get("Attente Notification Tour De Controle Fin De Vol").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), futureDate));
    }
}
