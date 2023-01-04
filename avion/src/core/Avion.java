package core;

import core.attente.Loi;
import core.evenements.*;
import core.protocole.Consigne;
import core.protocole.Message;
import core.radio.RadioClient;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

public class Avion extends SimEntity {
    public static HashMap<String, HashMap<String, Loi>> evenements = new HashMap<>(){{
        put("NotificationTourDeControleArrivee", NotificationTourDeControleArrivee.attentes);
        put("Approche", Approche.attentes);
        put("Atterissage", Atterissage.attentes);
        put("RoulementArrivee", RoulementArrivee.attentes);
        put("NotificationTourDeControleFinDeVol", NotificationTourDeControleFinDeVol.attentes);
        put("DechargementPassagers", DechargementPassagers.attentes);
        put("Atteri", Atteri.attentes);
        put("Embarquement", Embarquement.attentes);
        put("NotificationTourDeControleDepart", NotificationTourDeControleDepart.attentes);
        put("RoulementDepart", RoulementDepart.attentes);
        put("Decollage", Decollage.attentes);
        put("NotificationTourDeControleDecollage", NotificationTourDeControleDecollage.attentes);
    }};

    public enum eEtat {
        CIEL,
        APPROCHE,
        ATTERISSAGE,
        ROULEMENT_ARRIVEE,
        DECHARGEMENT_PASSAGERS,
        DECHARGEMENT_MATERIEL,
        ATTERI,
        EMBARQUEMENT,
        ROULEMENT_DEPART,
        DECOLLAGE
    }
    private eEtat etat;
    private RadioClient radio;
    private Consigne consigne;

    public Avion(SimuEngine eng) {
        super(eng);
        init();
    }

    @Override
    public void init()
    {
        etat = eEtat.CIEL;
        try {
            radio = new RadioClient(5430);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5));
        getEngine().postEvent(new NotificationTourDeControleArrivee(this, date));
    }

    public Message utiliserRadio(Message msg) throws IOException,
            ClassNotFoundException {
        radio.envoyerMessage(msg);
        return radio.recevoirMessage();
    }
    public Consigne getConsigne() {
        return consigne;
    }
    public void setConsigne(Consigne consigne) {
        this.consigne = consigne;
    }

    public eEtat getEtat() {
        return etat;
    }
    public void setEtat(eEtat etat) {
        this.etat = etat;
    }
}
