package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Embarquement extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Depart", new Gaussienne(0.1, 0.1));
    }};
    public Embarquement(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Embarquement";
    }

    @Override
    public String toString() {
        return "Embarquement";
    }

    @Override
    public void process() {
        LogicalDateTime futureDate = getDateOccurence().add(
                LogicalDuration.ofMinutes(
                        attentes.get("Attente Notification Tour De Controle Depart").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), futureDate));
    }
}