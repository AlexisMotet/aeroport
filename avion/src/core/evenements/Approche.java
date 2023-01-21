package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Approche extends EvenementAvion
{

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Approche", new Gaussienne(3.5, 1.5));
    }};

    public Approche(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Approche";
    }

    @Override
    public String toString() {
        return "Approche";
    }


    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.APPROCHE);
        LogicalDateTime futureDate = getDateOccurence().add(LogicalDuration.ofMinutes(
                attentes.get("duree Approche").next()));
        avion.getEngine().postEvent(new Atterissage(getEntity(), futureDate));
    }
}
