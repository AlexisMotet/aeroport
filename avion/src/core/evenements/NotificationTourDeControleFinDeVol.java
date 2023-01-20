package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import core.protocole.Message;
import core.protocole.MessageFinDeVol;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleFinDeVol extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Dechargement Passagers", new Gaussienne(0.1, 0.1));
        put("Attente Notification Tour De Controle Fin De Vol", new Gaussienne(5, 1));
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
                                "Attente Dechargement Passagers").next()));
                avion.getEngine().postEvent(new DechargementPassagers(getEntity(), futureDate));
            } else
            {
                LogicalDateTime futureDate = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get(
                                "Attente Notification Tour De Controle Fin De Vol").next()));
                avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), futureDate));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
