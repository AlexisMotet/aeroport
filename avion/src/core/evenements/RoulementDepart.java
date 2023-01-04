package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class RoulementDepart extends SimEvent {
    static private final Loi attenteRoulementDepart = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteRoulementDepart);
    }};
    private final Avion avion;
    public RoulementDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ROULEMENT_DEPART);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteRoulementDepart.next().longValue()));
        avion.getEngine().postEvent(new Decollage(getEntity(), date));
    }
}
