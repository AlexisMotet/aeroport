package core;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.LambdaSimEvent;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;

public class Avion extends SimEntity {
    public enum enumPosition {
        DEBUT_PISTE,
        MILIEU_PISTE,
        FIN_PISTE,
    }

    public enum enumPiste {
        NONE,
        TW1,
        TW2
    }

    public enumPosition position;
    public enumPiste piste;
    private TourDeControleParfaite tourDeControle;
    public Avion(SimuEngine eng, TourDeControleParfaite tourDeControle) {
        super(eng);
        this.tourDeControle = tourDeControle;
        Logger.Information(this, "", "creation avion");
        init();
    }

    @Override
    public void init()
    {
        piste = enumPiste.NONE;
        getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5)),
                this::notificationTourDeControleArrivee));
    }

    public void notificationTourDeControleArrivee()
    {
        TourDeControleParfaite.enumMessage msg = tourDeControle.communication(TourDeControleParfaite.enumMessage.ARRIVEE);
        if (msg == TourDeControleParfaite.enumMessage.OK)
        {
            Logger.Information(this, "", "TdC arrivee OK");
            LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(2));
            getEngine().postEvent(new LambdaSimEvent(this, date, this::approche));
        }
        else
        {
            Logger.Information(this, "", "TdC arrivee NOK");
            LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5));
            getEngine().postEvent(new LambdaSimEvent(this, date, this::notificationTourDeControleArrivee));
        }
    }

    public void attentePisteLibre()
    {
        Logger.Information(this, "", "attente piste libre");
        LogicalDuration attente = LogicalDuration.ofMinutes(5);
        LogicalDateTime date = getEngine().getCurrentDate().add(attente);
        getEngine().postEvent(new LambdaSimEvent(this, date, this::approche));
    }

    public void approche()
    {
        Logger.Information(this, "", "approche");
        position = enumPosition.DEBUT_PISTE;
        piste = enumPiste.TW1;
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5));
        getEngine().postEvent(new LambdaSimEvent(this, date, this::atterissage));

    }

    public void atterissage()
    {
        Logger.Information(this, "", "atterissage");
        position = enumPosition.MILIEU_PISTE;
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(2));
        getEngine().postEvent(new LambdaSimEvent(this, date, this::roulementArivee));

    }

    public void roulementArivee()
    {
        Logger.Information(this, "", "roulement");
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5));
        getEngine().postEvent(new LambdaSimEvent(this, date, this::notificationTourDeControleFinDeVol));
    }

    public void notificationTourDeControleFinDeVol()
    {
        position = enumPosition.FIN_PISTE;
        TourDeControleParfaite.enumMessage msg = tourDeControle.communication(TourDeControleParfaite.enumMessage.FIN_DE_VOL);
        if (msg == TourDeControleParfaite.enumMessage.OK)
        {
            Logger.Information(this, "", "TdC fin de vol OK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate(), this::dechargementPassagers));
        }
        else
        {
            Logger.Information(this, "", "TdC fin de vol NOK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(2)),
                    this::notificationTourDeControleArrivee));
        }
    }

    public void dechargementPassagers() {
        Logger.Information(this, "", "dechargement passagers");
        getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5)), this::dechargementMateriel));
    }

    public void dechargementMateriel()
    {
        Logger.Information(this, "", "dechargement materiel");
        getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5)), this::atteri));
    }

    public void atteri()
    {
        Logger.Information(this, "", "atteri");
        getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(30)), this::embarquement));
    }

    public void embarquement()
    {
        position = enumPosition.DEBUT_PISTE;
        piste = enumPiste.TW2;
        Logger.Information(this, "", "embarquement");
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(20));
        getEngine().postEvent(new LambdaSimEvent(this, date, this::notificationTourDeControleDepart));
    }

    public void notificationTourDeControleDepart()
    {
        TourDeControleParfaite.enumMessage msg = tourDeControle.communication(TourDeControleParfaite.enumMessage.DEPART);
        if (msg == TourDeControleParfaite.enumMessage.OK)
        {
            Logger.Information(this, "", "TdC depart OK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate(), this::roulementDepart));
        }
        else
        {
            Logger.Information(this, "", "TdC depart NOK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(2)),
                    this::notificationTourDeControleDepart));
        }
    }

    public void roulementDepart()
    {
        Logger.Information(this, "", "roulement depart");
        position = enumPosition.MILIEU_PISTE;
        LogicalDateTime date = getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5));
        getEngine().postEvent(new LambdaSimEvent(this, date, this::decollage));
    }

    public void decollage() {
        Logger.Information(this, "", "decollage");
        position = enumPosition.FIN_PISTE;
        piste = enumPiste.NONE;
        getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(5)),
                this::notificationTourDeControleDecollage));
    }

    public void notificationTourDeControleDecollage()
    {
        TourDeControleParfaite.enumMessage msg = tourDeControle.communication(TourDeControleParfaite.enumMessage.DECOLLAGE);
        if (msg == TourDeControleParfaite.enumMessage.OK)
        {
            Logger.Information(this, "", "TdC decollage OK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate(), this::attentePisteLibre));
        }
        else
        {
            Logger.Information(this, "", "TdC decollage NOK");
            getEngine().postEvent(new LambdaSimEvent(this, getEngine().getCurrentDate().add(LogicalDuration.ofMinutes(2)),
                    this::notificationTourDeControleDecollage));
        }
    }
}
