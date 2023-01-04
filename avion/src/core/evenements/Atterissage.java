package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class Atterissage extends SimEvent
{

    static private final Loi attenteRoulementArrivee = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente roulement arrivee", attenteRoulementArrivee);
    }};
    private final Avion avion;
    public Atterissage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.ATTERISSAGE);
        LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(2));
        avion.getEngine().postEvent(new RoulementArrivee(getEntity(), date));
    }
}