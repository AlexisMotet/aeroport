package core.attentes;

import core.elements.Aeroport;

import java.util.ArrayList;

public class Gaussienne extends Loi {
    private final Parametre mu = new Parametre("mu", 0, 60);
    private final Parametre sigma = new Parametre("sigma", 0, 60);
    public static double MU = 2;
    public static double SIGMA = 5;

    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(mu);
        add(sigma);
    }};

    public Gaussienne() {
        mu.setVal(MU);
        sigma.setVal(SIGMA);
    }

    public Gaussienne(double mu, double sigma) {
        this.mu.setVal(mu);
        this.sigma.setVal(sigma);
    }
    @Override
    public long next() {
        return (long) (Aeroport.getRandom().nextGaussian() * sigma.getVal() + mu.getVal());
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    @Override
    public String getNom() {
        return "Loi gaussienne";
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
