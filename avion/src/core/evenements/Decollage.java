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

public class Decollage extends EvenementAvion {
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Decollage", new Uniforme());
    }};
    private final Avion avion;

    public Decollage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Decollage";
    }

    @Override
    public String toString() {
        return "Decollage";
    }

    @Override
    public void process()
    {
        avion.setEtat(Avion.eEtat.PISTE_CONSIGNE);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attentes.get("Attente Notification Tour De Controle Decollage").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleDecollage(getEntity(), date));
    }
}
