package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class Embarquement extends SimEvent {

    static private final Loi attenteNotificationTourDeControleDepart = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteNotificationTourDeControleDepart);
    }};
    private final Avion avion;
    public Embarquement(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.EMBARQUEMENT);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteNotificationTourDeControleDepart.next().longValue()));
        avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), date));
    }
}