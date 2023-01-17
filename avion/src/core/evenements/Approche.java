package core.evenements;

import core.Avion;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Approche extends EvenementAvion
{
    private final Avion avion;

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Atterissage", new Uniforme());
    }};

    public Approche(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
        avion = (Avion) entite;
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
        System.out.println(attentes.get("Attente Atterissage"));
        LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(
                attentes.get("Attente Atterissage").next()));
        avion.getEngine().postEvent(new Atterissage(getEntity(), date));
    }
}
