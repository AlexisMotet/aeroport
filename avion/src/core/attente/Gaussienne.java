package core.attente;

import java.util.ArrayList;

public class Gaussienne extends Loi {
    private final Parametre mu = new Parametre("mu", 0, 10);
    private final Parametre sigma = new Parametre("sigma", 0, 10);
    public static double MU = 2;
    public static double SIGMA = 5;

    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(mu);
        add(sigma);
    }};
    @Override
    public String getNom() {
        return "Gaussienne";
    }

    public Gaussienne() {
        this.mu.setVal(MU);
        this.sigma.setVal(SIGMA);
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    @Override
    public long next() {
        return (long) (getRandom().nextGaussian() * sigma.getVal() + mu.getVal());
    }

    @Override
    public Double getEsperance() {
        return mu.getVal();
    }

    @Override
    public Double getEcartType() {
        return sigma.getVal();
    }
}
