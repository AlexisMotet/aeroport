package core.evenements;

import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.protocole.Message;
import core.protocole.MessageDepart;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDepart extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente avant nouvelle notification", new Gaussienne(10, 2));
        put("attente avant Roulement Depart", new Gaussienne(0.1, 0.1));

    }};

    public NotificationTourDeControleDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Notification Tour De Controle Depart";
    }

    @Override
    public String toString() {
        return "Notification Tour De Controle Depart";
    }

    @Override
    public void process() {
        try
        {
            Message message = avion.utiliserRadio(new MessageDepart(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                LogicalDateTime dateDepart = getDateOccurence().add(LogicalDuration.ofMinutes(
                        attentes.get("attente avant Roulement Depart").next()));
                avion.getEngine().postEvent(new RoulementDepart(getEntity(),
                        dateDepart));
            }
            else
            {
                LogicalDuration retard = LogicalDuration.ofMinutes(attentes.get(
                        "attente avant nouvelle notification").next());
                avion.ajouterRetardDecollage((long) retard.getTotalOfSeconds());
                LogicalDateTime futureDate = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), futureDate));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}