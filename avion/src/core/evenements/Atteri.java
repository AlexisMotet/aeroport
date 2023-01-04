package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class Atteri extends SimEvent {

    static private final Loi attenteEmbarquement = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente atteri", attenteEmbarquement);
    }};
    private final Avion avion;
    public Atteri(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ATTERI);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteEmbarquement.next().longValue()));
        avion.getEngine().postEvent(new Embarquement(getEntity(), date));
    }
}
