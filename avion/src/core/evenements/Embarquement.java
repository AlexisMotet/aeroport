package core.evenements;

import core.attentes.Gaussienne;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Embarquement extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente avant Notification Tour De Controle Depart", new Gaussienne(0.1, 0.1));
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
        avion.setDateEmbarquement(getDateOccurence());
        LogicalDateTime futureDate = getDateOccurence().add(
                LogicalDuration.ofMinutes(
                        attentes.get("attente avant Notification Tour De Controle Depart").next()));
        avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), futureDate));
    }
}