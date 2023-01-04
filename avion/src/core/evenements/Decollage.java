package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class Decollage extends SimEvent {
    static private final Loi attenteDecollage = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteDecollage);
    }};
    private final Avion avion;

    public Decollage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.DECOLLAGE);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteDecollage.next().longValue()));
        avion.getEngine().postEvent(new NotificationTourDeControleDecollage(getEntity(), date));
    }
}
