package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Atterissage extends EvenementAvion
{

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Roulement Arrivee", new Gaussienne(2, 0));
    }};
    public Atterissage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Atterissage";
    }

    @Override
    public String toString() {
        return "Atterissage";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.ATTERISSAGE);
        LogicalDuration futureDate = LogicalDuration.ofMinutes(
                attentes.get("Attente Roulement Arrivee").next());
        LogicalDateTime date = getDateOccurence().add(futureDate);
        avion.getEngine().postEvent(new RoulementArrivee(getEntity(), date));
    }
}