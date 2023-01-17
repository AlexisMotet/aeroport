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

public class RoulementDepart extends EvenementAvion {
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Decollage", new Uniforme());
    }};
    private final Avion avion;
    public RoulementDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static String getNom(){
        return "Roulement Depart";
    }

    @Override
    public String toString() {
        return "Roulement Depart";
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ROULEMENT_DEPART);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attentes.get("Attente Decollage").next()));
        avion.getEngine().postEvent(new Decollage(getEntity(), date));
    }
}
