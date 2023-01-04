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

public class Atteri extends EvenementAvion {

    static private final Loi attenteEmbarquement = new Uniforme(10, 3 );
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Embarquement", attenteEmbarquement);
    }};
    private final Avion avion;
    public Atteri(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Atteri";
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ATTERI);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attenteEmbarquement.next().longValue()));
        avion.getEngine().postEvent(new Embarquement(getEntity(), date));
    }
}
