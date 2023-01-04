package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.protocole.Message;
import core.protocole.MessageFinDeVol;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleFinDeVol extends SimEvent
{
    static private final Loi attenteDechargementPassagers = new Exponentielle(10);
    static private final Loi attenteNotificationTourDeControleFinDeVol = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteDechargementPassagers);
        put("attente decollag", attenteNotificationTourDeControleFinDeVol);
    }};
    private final Avion avion;
    public NotificationTourDeControleFinDeVol(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
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
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attenteDechargementPassagers.next().longValue()));
                avion.getEngine().postEvent(new DechargementPassagers(getEntity(), date));
            } else
            {
                LogicalDateTime date = getDateOccurence().add(LogicalDuration.ofMinutes(
                        attenteNotificationTourDeControleFinDeVol.next().longValue()));
                avion.getEngine().postEvent(new NotificationTourDeControleFinDeVol(getEntity(), date));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
