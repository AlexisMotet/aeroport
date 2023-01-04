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
    public static final HashMap<String, HashMap<String, Loi>> attentesEvenements = new HashMap<>(){{
        put("NotificationTourDeControleArrivee", NotificationTourDeControleArrivee.getAttentes());
        put("Approche", Approche.getAttentes());
        put("Atterissage", Atterissage.getAttentes());
        put("RoulementArrivee", RoulementArrivee.getAttentes());
        put("NotificationTourDeControleFinDeVol", NotificationTourDeControleFinDeVol.getAttentes());
        put("DechargementPassagers", DechargementPassagers.getAttentes());
        put("Atteri", Atteri.getAttentes());
        put("Embarquement", Embarquement.getAttentes());
        put("NotificationTourDeControleDepart", NotificationTourDeControleDepart.getAttentes());
        put("RoulementDepart", RoulementDepart.getAttentes());
        put("Decollage", Decollage.getAttentes());
        put("NotificationTourDeControleDecollage", NotificationTourDeControleDecollage.getAttentes());
    }};

    public enum eEtat {
        CIEL,
        APPROCHE,
        ATTERISSAGE,
        ROULEMENT_ARRIVEE,
        DECHARGEMENT_PASSAGERS,
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
        getEngine().postEvent(new NotificationTourDeControleArrivee(this, getEngine().getCurrentDate()));
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
