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
import java.util.LinkedHashMap;

public class Avion extends SimEntity {
    public static final LinkedHashMap<String, HashMap<String, Loi>> attentesEvenements = new LinkedHashMap<>() {{
        put(NotificationTourDeControleArrivee.getNom(),
                NotificationTourDeControleArrivee.getAttentes());
        put(Approche.getNom(), Approche.getAttentes());
        put(Atterissage.getNom(), Atterissage.getAttentes());
        put(RoulementArrivee.getNom(), RoulementArrivee.getAttentes());
        put(NotificationTourDeControleFinDeVol.getNom(),
                NotificationTourDeControleFinDeVol.getAttentes());
        put(DechargementPassagers.getNom(), DechargementPassagers.getAttentes());
        put(Embarquement.getNom(), Embarquement.getAttentes());
        put(NotificationTourDeControleDepart.getNom(),
                NotificationTourDeControleDepart.getAttentes());
        put(RoulementDepart.getNom(), RoulementDepart.getAttentes());
        put(Decollage.getNom(), Decollage.getAttentes());
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

    private LogicalDuration retardAtterrissage = null;
    private LogicalDuration retardDecollage = null;

    private LogicalDuration retardAtterrissageFinal = null;

    private LogicalDuration retardDecollageFinal = null;

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

    public void ajouterRetardAtterissage(LogicalDuration duree) {
        if (retardAtterrissage == null) retardAtterrissage = LogicalDuration.ZERO;
        retardAtterrissage = retardAtterrissage.add(duree);
    }

    public void ajouterRetardDecollage(LogicalDuration duree) {
        if (retardDecollage == null) retardDecollage = LogicalDuration.ZERO;
        retardDecollage = retardDecollage.add(duree);
    }

    public void setRetardAtterrissage(LogicalDuration retardAtterrissage) {
        this.retardAtterrissage = retardAtterrissage;
    }

    public void setRetardDecollage(LogicalDuration retardDecollage) {
        this.retardDecollage = retardDecollage;
    }

    public LogicalDuration getRetardAtterrissage() {
        return retardAtterrissage;
    }

    public LogicalDuration getRetardDecollage() {
        return retardDecollage;
    }
    public LogicalDuration getRetardAtterrissageFinal() {
        LogicalDuration sauvegarde = retardAtterrissageFinal;
        retardAtterrissageFinal = null;
        return sauvegarde;
    }

    public void setRetardAtterrissageFinal(LogicalDuration retardAtterrissageFinal) {
        this.retardAtterrissageFinal = retardAtterrissageFinal;
    }

    public LogicalDuration getRetardDecollageFinal() {
        LogicalDuration sauvegarde = retardDecollageFinal;
        retardDecollageFinal = null;
        return sauvegarde;
    }

    public void setRetardDecollageFinal(LogicalDuration retardDecollageFinal) {
        this.retardDecollageFinal = retardDecollageFinal;
    }

}

