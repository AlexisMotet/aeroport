package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Loi {

    public abstract eLoi getNom();
    private final MoreRandom random = new MoreRandom();
    public MoreRandom getRandom() {
        return random;
    }
    public abstract ArrayList<Parametre> getParametres();

    public abstract long next();

    public abstract Double getEsperance();

}
