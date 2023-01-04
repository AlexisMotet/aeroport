package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.ArrayList;
import java.util.HashMap;

public class Exponentielle extends Loi{
    private final Parametre lambda = new Parametre("lambda", 0, 10);

    public Exponentielle(double lambda) {
        this.lambda.setVal(lambda);
    }

    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(lambda);
    }};

    @Override
    public Double next(){
        return getRandom().nextExp(lambda.getVal());
    }

    @Override
    public String getNom() {
        return "Exponentielle";
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    public Double getEsperance()
    {
        return null;
    }
}
