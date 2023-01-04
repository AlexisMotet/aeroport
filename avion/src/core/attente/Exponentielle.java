package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.HashMap;

public class Exponentielle extends Loi{
    private double lambda;

    public Exponentielle(double lambda) {

        nom = "Exponentielle";
        this.lambda = lambda;
    }

    private final HashMap<String, Double> parametres = new HashMap<>(){{
        put("lambda", lambda);
    }};

    @Override
    public Double next(){
        return getRandom().nextExp(lambda);
    }
    @Override
    public HashMap<String, Double> getParametres() {
        return parametres;
    }
}
