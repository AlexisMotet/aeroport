package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Approche extends EvenementAvion
{

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Atterissage", new Gaussienne(3.5, 1.5));
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
        Logger.Information(avion, "", "");
        avion.setEtat(Avion.eEtat.APPROCHE);
        LogicalDateTime futureDate = getDateOccurence().add(LogicalDuration.ofMinutes(
                attentes.get("Attente Atterissage").next()));
        avion.getEngine().postEvent(new Atterissage(getEntity(), futureDate));
    }
}
