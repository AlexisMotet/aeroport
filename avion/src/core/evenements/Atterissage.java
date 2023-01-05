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

public class Atterissage extends EvenementAvion
{

    static private final Loi attenteRoulementArrivee = new Uniforme(10, 3 );
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Roulement Arrivee", attenteRoulementArrivee);
    }};
    private final Avion avion;
    public Atterissage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Atterissage";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.ATTERISSAGE);
        LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(2));
        avion.getEngine().postEvent(new RoulementArrivee(getEntity(), date));
    }
}