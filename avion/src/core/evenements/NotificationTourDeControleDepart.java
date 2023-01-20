package core.evenements;

import core.Avion;
import core.attente.Gaussienne;
import core.attente.Loi;
import core.attente.Uniforme;
import core.outils.OutilDate;
import core.protocole.Message;
import core.protocole.MessageDepart;
import core.protocole.eMessage;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

import java.io.IOException;
import java.util.HashMap;

public class NotificationTourDeControleDepart extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Roulement Depart", new Gaussienne(0.1, 0.1));
        put("Attente Notification Tour De Controle Depart", new Gaussienne(10, 2));
    }};

    public NotificationTourDeControleDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
    }

    public static HashMap<String, Loi> getAttentes() {
        return attentes;
    }

    public static String getNom(){
        return "Notification Tour De Controle Depart";
    }

    @Override
    public String toString() {
        return "Notification Tour De Controle Depart";
    }

    @Override
    public void process() {
        try
        {
            Message message = avion.utiliserRadio(new MessageDepart(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            LogicalDateTime dateDepart = getDateOccurence().add(LogicalDuration.ofMinutes(
                    attentes.get("Attente Roulement Depart").next()));
            if (msg == eMessage.MessageOk)
            {
                if (!OutilDate.checkSiJour(dateDepart))
                {
                    avion.getEngine().postEvent(new RoulementDepart(getEntity(),
                            OutilDate.obtenirProchainMatin(dateDepart)));
                }
                else
                {
                    avion.getEngine().postEvent(new RoulementDepart(getEntity(),
                            dateDepart));
                }
            }
            else
            {
                LogicalDuration retard = LogicalDuration.ofMinutes(attentes.get(
                        "Attente Notification Tour De Controle Depart").next());
                avion.ajouterRetardDecollage((long) retard.getTotalOfSeconds());
                LogicalDateTime futureDate = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), futureDate));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}