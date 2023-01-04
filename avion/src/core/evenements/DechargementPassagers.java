package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class DechargementPassagers extends SimEvent {

    static private final Loi attenteAtteri = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente atteri", attenteAtteri);
    }};
    private final Avion avion;
    public DechargementPassagers(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.DECHARGEMENT_PASSAGERS);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteAtteri.next().longValue()));
        avion.getEngine().postEvent(new Atteri(getEntity(), date));
    }
}
