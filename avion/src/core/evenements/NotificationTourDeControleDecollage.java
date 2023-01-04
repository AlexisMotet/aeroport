package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.protocole.Message;
import core.protocole.MessageDepart;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDecollage extends SimEvent
{
    static private final Loi attenteApproche = new Exponentielle(10);
    static private final Loi attenteNotificationTourDeControleArrivee = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteApproche);
        put("attente decollag", attenteNotificationTourDeControleArrivee);
    }};

    private final Avion avion;
    public NotificationTourDeControleDecollage(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }
    @Override
    public void process()
    {
        try
        {
            Message message = avion.utiliserRadio(new MessageDepart(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(20));
                avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), date));
            } else
            {
                LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(5));
                avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), date));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
