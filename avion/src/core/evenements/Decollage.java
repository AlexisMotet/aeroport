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

public class Decollage extends EvenementAvion {
    static private final Loi attenteNotificationTourDeControleDecollage = new Uniforme(10, 3 );
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Decollage", attenteNotificationTourDeControleDecollage);
    }};
    private final Avion avion;

    public Decollage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Decollage";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.DECOLLAGE);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteNotificationTourDeControleDecollage.next().longValue()));
        avion.getEngine().postEvent(new NotificationTourDeControleDecollage(getEntity(), date));
    }
}
