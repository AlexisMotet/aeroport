package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.ArrayList;
import java.util.HashMap;

public class Exponentielle extends Loi{

    public static double LAMBDA = 1;
    private final Parametre lambda = new Parametre("lambda", 0, 10);

    public Exponentielle() {
        lambda.setVal(LAMBDA);
    }

    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(lambda);
    }};

    @Override
    public long next(){
        return (long) getRandom().nextExp(lambda.getVal());
    }

    @Override
    public eLoi getNom() {
        return eLoi.LOI_EXPONENTIELLE;
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    public Double getEsperance()
    {
        return 1/lambda.getVal();
    }
}
