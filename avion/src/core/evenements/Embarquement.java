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

public class Embarquement extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Depart", new Uniforme());
    }};
    private final Avion avion;
    public Embarquement(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Embarquement";
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.EMBARQUEMENT);
        LogicalDateTime date = getDateOccurence().add(
                LogicalDuration.ofMinutes(attentes.get("Attente Notification Tour De Controle Depart").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), date));
    }
}