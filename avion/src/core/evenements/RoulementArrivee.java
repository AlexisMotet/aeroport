package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class RoulementArrivee extends EvenementAvion
{
    static private final Loi attenteNotificationTourDeControleFinDeVol = new Uniforme(10, 3 );
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Fin De Vol", attenteNotificationTourDeControleFinDeVol);
    }};
    private final Avion avion;
    public RoulementArrivee(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Roulement Arrivee";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.ROULEMENT_ARRIVEE);
        LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(
                attenteNotificationTourDeControleFinDeVol.next().longValue()));
        avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), date));
    }
}
