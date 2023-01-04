package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.HashMap;

public abstract class Loi {

    String nom;

    public String getNom() {
        return nom;
    }

    private final MoreRandom random = new MoreRandom();
    public MoreRandom getRandom() {
        return random;
    }

    public abstract HashMap<String, Double> getParametres();

    public abstract Double next();

}
