package core;

import core.evenements.CreationAvion;
import core.outils.OutilDate;
import core.outils.Affluence;
import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;

import java.util.Random;

public class EntiteSupreme extends SimEntity {
    private final MoreRandom random = new MoreRandom();
    public EntiteSupreme(SimuEngine eng) {
        super(eng);
        init();
    }
    @Override
    public void init() {
        getEngine().postEvent(new CreationAvion(this,
                getEngine().getCurrentDate()));
    }
    public long getFrequence()
    {
        LogicalDateTime date = getEngine().getCurrentDate();
        Affluence affluence = OutilDate.obtenirAffluence(date);
        switch (affluence) {
            case HEURE_DE_POINTE -> {
                double mu = 20;
                double sigma = mu / 10;
                return (long) (random.nextGaussian() * sigma + mu);
            }
            case WEEK_END -> {
                double mu = 40;
                double sigma = mu / 10;
                return (long) (random.nextGaussian() * sigma + mu);
            }
            case NORMALE -> {
                double mu = 10;
                double sigma = mu / 10;
                return (long) (random.nextGaussian() * sigma + mu);
            }
            default -> {return 60;}
        }
    }

    @Override
    public String toString() {
        return "L'Entite Supreme";
    }
}
