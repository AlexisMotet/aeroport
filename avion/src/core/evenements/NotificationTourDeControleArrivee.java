package core.evenements;

import core.Avion;
import core.attente.Loi;
import core.attente.Uniforme;
import core.outils.OutilDate;
import core.protocole.Message;
import core.protocole.MessageArrivee;
import core.protocole.MessageVoieArrivee;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

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
        try
        {
            LogicalDateTime dateArrivee = getDateOccurence().add(LogicalDuration.ofMinutes(
                    attentes.get("Attente Notification Tour De Controle Arrivee").next()));
            if (OutilDate.checkSiJour(dateArrivee)) {
                Message message = avion.utiliserRadio(new MessageArrivee());
                eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
                if (msg == eMessage.MessageVoieArrivee) {
                    avion.setConsigne(((MessageVoieArrivee) message).getConsigne());
                    avion.setEtat(Avion.eEtat.CIEL_CONSIGNE_ARRIVEE);
                    avion.getEngine().postEvent(new Approche(getEntity(), dateArrivee));
                }
                else {
                    LogicalDuration retard = LogicalDuration.ofMinutes(attentes.get(
                            "Attente Notification Tour De Controle Arrivee").next());
                    avion.ajouterRetardAtterissage((long)retard.getTotalOfSeconds());
                    LogicalDateTime date = getDateOccurence().add(retard);
                    avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(), date));
                }
            }
            else
            {
                avion.getEngine().postEvent(new NotificationTourDeControleArrivee(getEntity(),
                        OutilDate.obtenirProchainMatin(getDateOccurence())));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}