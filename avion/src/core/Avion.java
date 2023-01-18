package core;

import core.attente.Loi;
import core.evenements.*;
import core.protocole.Consigne;
import core.protocole.Message;
import core.radio.RadioClient;
 import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Avion extends SimEntity {
    public static final LinkedHashMap<String, HashMap<String, Loi>> attentesEvenements = new LinkedHashMap<>() {{
        put(NotificationTourDeControleArrivee.getNom(),
                NotificationTourDeControleArrivee.getAttentes());
        put(Approche.getNom(),
                Approche.getAttentes());
        put(Atterissage.getNom(),
                Atterissage.getAttentes());
        put(RoulementArrivee.getNom(),
                RoulementArrivee.getAttentes());
        put(NotificationTourDeControleFinDeVol.getNom(),
                NotificationTourDeControleFinDeVol.getAttentes());
        put(DechargementPassagers.getNom(),
                DechargementPassagers.getAttentes());
        put(Embarquement.getNom(),
                Embarquement.getAttentes());
        put(NotificationTourDeControleDepart.getNom(),
                NotificationTourDeControleDepart.getAttentes());
        put(RoulementDepart.getNom(),
                RoulementDepart.getAttentes());
        put(Decollage.getNom(),
                Decollage.getAttentes());
        put(NotificationTourDeControleDecollage.getNom(),
                NotificationTourDeControleDecollage.getAttentes());
    }};

    public enum eEtat {
        CIEL,
        CIEL_CONSIGNE_ARRIVEE,
        APPROCHE,
        ATTERISSAGE,
        ROULEMENT_ARRIVEE,
        ATTERI,
        ROULEMENT_DEPART,
        PISTE_CONSIGNE,
        CIEL_DEPART,
    }

    private eEtat etat;
    private RadioClient radio;
    private Consigne consigne;
    private long retardAtterrissage = -1;
    private long retardDecollage = -1;

    public Avion(SimuEngine eng) {
        super(eng);
        init();
    }

    @Override
    public void init() {
        try {
            radio = new RadioClient(5430);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        setEtat(Avion.eEtat.CIEL);
        setConsigne(null);
        getEngine().postEvent(new NotificationTourDeControleArrivee(this,
                getEngine().getCurrentDate()));
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

    public void ajouterRetardAtterissage(long duree) {
        if (retardAtterrissage == -1) retardAtterrissage = 0;
        retardAtterrissage += duree;
    }

    public void ajouterRetardDecollage(long duree) {
        if (retardDecollage == -1) retardDecollage = 0;
        retardDecollage += duree;
    }
    public long getRetardAtterrissage() {
        if (retardAtterrissage!= -1 && etat == eEtat.CIEL_CONSIGNE_ARRIVEE)
        {
            long sauv = retardAtterrissage;
            retardAtterrissage = -1;
            return sauv;
        }
        return -1;
    }
    public long getRetardDecollage() {
        if (retardDecollage!= -1 && etat == eEtat.CIEL_DEPART)
        {
            long sauv = retardDecollage;
            retardDecollage = -1;
            return sauv;
        }
        return -1;
    }
}

