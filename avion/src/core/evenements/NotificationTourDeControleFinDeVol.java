package core.evenements;

import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.protocole.Message;
import core.protocole.MessageFinDeVol;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleFinDeVol extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente avant nouvelle notification", new Gaussienne(0.1, 0.1));
        put("attente avant Dechargement Passagers", new Gaussienne(5, 1));

    }};
    public NotificationTourDeControleFinDeVol(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Notification Tour De Controle Fin De Vol";
    }

    @Override
    public String toString() {
        return "Notification Tour De Controle Fin De Vol";
    }

    @Override
    public void process()
    {
        try
        {
            Message message = avion.utiliserRadio(new MessageFinDeVol(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                LogicalDateTime futureDate = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get(
                                "attente avant Dechargement Passagers").next()));
                avion.getEngine().postEvent(new DechargementPassagers(getEntity(), futureDate));
            } else
            {
                LogicalDateTime futureDate = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get(
                                "attente avant nouvelle notification").next()));
                avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), futureDate));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
