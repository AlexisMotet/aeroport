package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.outils.OutilDate;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public class Atterissage extends EvenementAvion
{

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("duree Atterissage", new Gaussienne(2, 0));
    }};
    public Atterissage(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom() {
        return "Atterissage";
    }

    @Override
    public String toString() {
        return "Atterissage";
    }

    @Override
    public void process()
    {
        LogicalDateTime date = getDateOccurence();
        if (OutilDate.checkSiWeekEnd(date)){
            Logger.Information(getEntity(), "", String.format("atterissage week-end; %s; %s",
                    date.getDayOfWeek(), OutilDate.obtenirHeure(date)));
        }
        else {
            Logger.Information(getEntity(), "", String.format("atterissage semaine; %s; %s",
                    date.getDayOfWeek(), OutilDate.obtenirHeure(date)));
        }
        avion.setEtat(Avion.eEtat.ATTERISSAGE);
        LogicalDuration futureDate = LogicalDuration.ofMinutes(
                attentes.get("duree Atterissage").next());
        avion.getEngine().postEvent(new RoulementArrivee(getEntity(),
                date.add(futureDate)));
    }
}