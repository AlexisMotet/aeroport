package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.protocole.Message;
import core.protocole.MessageArrivee;
import core.protocole.MessageVoieArrivee;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleArrivee extends SimEvent
{
    static private final Loi attenteApproche = new Exponentielle(10);
    static private final Loi attenteNotificationTourDeControleArrivee = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteApproche);
        put("attente decollag", attenteNotificationTourDeControleArrivee);
    }};
    private final Avion avion;
    public NotificationTourDeControleArrivee(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process() {
        try
        {
            Message message = avion.utiliserRadio(new MessageArrivee());
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageVoieArrivee)
            {
                avion.setConsigne(((MessageVoieArrivee) message).getConsigne());
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attenteApproche.next().longValue()));
                avion.getEngine().postEvent(new Approche(getEntity(), date));
            } else
            {
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attenteNotificationTourDeControleArrivee.next().longValue()));
                avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), date));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}