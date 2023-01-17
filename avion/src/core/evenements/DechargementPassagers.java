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

public class DechargementPassagers extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Atteri", new Uniforme());
    }};
    private final Avion avion;
    public DechargementPassagers(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Dechargement Passagers";
    }

    @Override
    public String toString() {
        return "Dechargement Passagers";
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.ATTERI);
        LogicalDuration dureeAttente = LogicalDuration.ofMinutes(
                attentes.get("Attente Atteri").next());
        LogicalDateTime date = getDateOccurence().add(dureeAttente);
        avion.getEngine().postEvent(new Embarquement(getEntity(), date));
    }
}
