package core.evenements;

import core.Avion;
import core.attente.Exponentielle;
import core.attente.Loi;
import core.attente.Uniforme;
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

public class NotificationTourDeControleDepart extends EvenementAvion {

    static private final HashMap<String, Loi> attentes = new HashMap<>(){{
        put("Attente Roulement Depart", new Uniforme());
        put("Attente Notification Tour De Controle Depart", new Uniforme());
    }};

    private final Avion avion;
    public NotificationTourDeControleDepart(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;

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
            LogicalDuration retard;
            Message message = avion.utiliserRadio(new MessageDepart(avion.getConsigne()));
            eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
            if (msg == eMessage.MessageOk)
            {
                retard = LogicalDuration.ofMinutes(attentes.get("Attente Roulement Depart").next());
                avion.ajouterRetardDecollage(retard);
                avion.setRetardDecollageFinal(avion.getRetardDecollage());
                avion.setRetardDecollage(LogicalDuration.ZERO);
                LogicalDateTime date = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new RoulementDepart(getEntity(), date));
            } else
            {
                retard = LogicalDuration.ofMinutes(attentes.get(
                        "Attente Notification Tour De Controle Depart").next());
                avion.ajouterRetardDecollage(retard);
                LogicalDateTime date = getDateOccurence().add(retard);
                avion.getEngine().postEvent(new NotificationTourDeControleDepart(getEntity(), date));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}