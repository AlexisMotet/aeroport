package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.ArrayList;
import java.util.HashMap;

public class Triangle extends Loi {
    private final Parametre a = new Parametre("a", 0, 10);
    private final Parametre b = new Parametre("b", 0, 10);
    private final Parametre c = new Parametre("c", 0, 10);

    public Triangle(double a, double b, double c) {
        this.a.setVal(a);
        this.b.setVal(b);
        this.c.setVal(c);
    }
    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(a);
        add(b);
        add(c);
    }};

    @Override
    public Double next(){
        return getRandom().nextTriangle(a.getVal(), b.getVal(), c.getVal());
    }

    @Override
    public String getNom() {
        return "Triangle";
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    public Double getEsperance()
    {
        if (a.getVal() > b.getVal())
        {
            return null;
        }
        else
        {
            return (a.getVal() + b.getVal())/2;
        }
    }
}
