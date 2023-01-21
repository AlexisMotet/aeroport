package core.evenements;

import core.Avion;
import core.attentes.Gaussienne;
import core.attentes.Loi;
import core.outils.OutilDate;
import core.protocole.Message;
import core.protocole.MessageArrivee;
import core.protocole.MessageVoieArrivee;
import core.protocole.eMessage;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleArrivee extends EvenementAvion
{

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("attente avant nouvelle notification",
                new Gaussienne(20, 5));
        put("attente avant Approche", new Gaussienne(0.1, 0.1));

    }};
    public NotificationTourDeControleArrivee(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Notification Tour De Controle Arrivee";
    }

    @Override
    public String toString() {
        return "Notification Tour De Controle Arrivee";
    }

    @Override
    public void process() {
        avion.setEtat(Avion.eEtat.CIEL);
        LogicalDateTime date = getDateOccurence();
        avion.setDateArrivee(date);
        if (!OutilDate.checkSiJour(date))
        {
            avion.getEngine().postEvent(
                    new AutoDestruction(avion, date));
        }
        else {
            try
            {
                Message message = avion.utiliserRadio(new MessageArrivee());
                eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
                if (msg == eMessage.MessageVoieArrivee) {
                    avion.setConsigne(((MessageVoieArrivee) message).getConsigne());
                    avion.setEtat(Avion.eEtat.CIEL_CONSIGNE_ARRIVEE);
                    LogicalDateTime futureDate = date.add(
                            LogicalDuration.ofMinutes(
                                    attentes.get("attente avant Approche").next()));
                    avion.getEngine().postEvent(new Approche(getEntity(), futureDate));
                }
                else {
                    LogicalDuration retard = LogicalDuration.ofMinutes(attentes.get(
                            "attente avant nouvelle notification").next());
                    avion.ajouterRetardAtterissage((long)retard.getTotalOfSeconds());
                    LogicalDateTime futureDate = date.add(retard);
                    avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), futureDate));
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}