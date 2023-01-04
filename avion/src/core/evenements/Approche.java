package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.util.HashMap;

public class Approche extends SimEvent
{
    private final Avion avion;

    static private final Loi attenteAtterissage = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente atterissage", attenteAtterissage);
    }};

    public Approche(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.APPROCHE);
        LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(
                attenteAtterissage.next().longValue()));
        avion.getEngine().postEvent(new Atterissage(getEntity(), date));
    }
}
