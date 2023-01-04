package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.protocole.Message;
import core.protocole.MessageDepart;
import core.protocole.MessageFinDeVol;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDepart extends SimEvent {

    static private final Loi attenteRoulementDepart = new Exponentielle(10);
    static private final Loi attenteNotificationTourDeControleDepart = new Exponentielle(10);
    public static HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente decollage", attenteRoulementDepart);
        put("attente decollag", attenteNotificationTourDeControleDepart);
    }};

    private final Avion avion;
    public NotificationTourDeControleDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;

    }
    @Override
    public void process() {
        try
        {
            Message message = avion.utiliserRadio(new MessageDepart(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attenteRoulementDepart.next().longValue()));
                avion.getEngine().postEvent(new RoulementDepart(getEntity(), date));
            } else
            {
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attenteNotificationTourDeControleDepart.next().longValue()));
                avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), date));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}