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
    static private final Loi attenteDecollage = new Uniforme(10, 3 );
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Decollage", attenteDecollage);
    }};
    private final Avion avion;
    public RoulementDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
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
                LogicalDuration.ofMinutes(attenteDecollage.next().longValue()));
        avion.getEngine().postEvent(new Decollage(getEntity(), date));
    }
}
