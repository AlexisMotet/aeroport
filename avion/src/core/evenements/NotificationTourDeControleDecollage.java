package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.protocole.Message;
import core.protocole.MessageDecollage;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDecollage extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente avant nouvelle notification", new Gaussienne(3, 0.1));
    }};

    public NotificationTourDeControleDecollage(SimEntity entite, LogicalDateTime dateOccurence)
    {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Notification Tour De Controle Decollage";
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
            Message message = avion.utiliserRadio(new MessageDecollage(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                avion.setEtat(Avion.eEtat.CIEL_DEPART);
                avion.setDateDecollage(getDateOccurence());
                avion.getEngine().postEvent(new AutoDestruction(getEntity(),
                        getDateOccurence()));
            }
            else {
                LogicalDateTime futureDate = getDateOccurence().add(
                        LogicalDuration.ofMinutes(attentes.get(
                                "attente avant nouvelle notification").next()));
                avion.getEngine().postEvent(new NotificationTourDeControleDecollage(getEntity(),
                        futureDate));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
