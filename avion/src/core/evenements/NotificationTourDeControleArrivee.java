package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.attente.Uniforme;
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

public class NotificationTourDeControleArrivee extends EvenementAvion
{
    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Approche", new Uniforme());
        put("Attente Notification Tour De Controle Arrivee", new Uniforme());
    }};
    private final Avion avion;
    public NotificationTourDeControleArrivee(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
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
        avion.setConsigne(null);
        try
        {
            LogicalDuration retard;
            Message message = avion.utiliserRadio(new MessageArrivee());
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageVoieArrivee)
            {
                avion.setConsigne(((MessageVoieArrivee) message).getConsigne());
                avion.setEtat(Avion.eEtat.CIEL_CONSIGNE_ARRIVEE);
                retard = LogicalDuration.ofMinutes(attentes.get(
                        "Attente Approche").next());
                avion.ajouterRetardAtterissage(retard);
                avion.setRetardAtterrissageFinal(avion.getRetardAtterrissage());
                avion.setRetardAtterrissage(LogicalDuration.ZERO);
                LogicalDateTime date = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new Approche(getEntity(), date));
            } else
            {
                retard = LogicalDuration.ofMinutes(attentes.get(
                                "Attente Notification Tour De Controle Arrivee").next());
                avion.ajouterRetardAtterissage(retard);
                LogicalDateTime date = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), date));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}