package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.attente.Uniforme;
import core.protocole.Message;
import core.protocole.MessageDepart;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDecollage extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Notification Tour De Controle Arrivee", new Uniforme());
        put("Attente Notification Tour De Controle Decollage", new Uniforme());
    }};

    private final Avion avion;
    public NotificationTourDeControleDecollage(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    @Override
    public String toString() {
        return "Notification Tour De Controle Decollage";
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
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get("Attente Notification Tour De Controle Arrivee").next()));
                avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), date));
            } else
            {
                LogicalDateTime date = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get("Attente Notification Tour De Controle Decollage").next()));
                avion.getEngine().postEvent(new NotificationTourDeControleDecollage(getEntity(), date));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
