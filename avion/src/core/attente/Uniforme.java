package core.attente;

import java.util.ArrayList;
import java.util.HashMap;

public class Uniforme extends Loi {
    private final Parametre a = new Parametre("a", 0, 60);
    private final Parametre b = new Parametre("b", 0, 60);

    public static double A = 2;
    public static double B = 5;

    public Uniforme() {
        a.setVal(A);
        b.setVal(B);
    }

    public Uniforme(double a, double b) {
        this.a.setVal(a);
        this.b.setVal(b);
    }
    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(a);
        add(b);
    }};

    @Override
    public long next(){
        if (a.getVal() < b.getVal())
        {
            return (long) getRandom().nextUniform(a.getVal(), b.getVal());
        }
        else
        {
            return 60;
        }
    }

    @Override
    public String getNom() {
        return "Loi uniforme";
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }
    @Override
    public Double getEsperance()
    {
        if (a.getVal() <= b.getVal())
        {
            return (a.getVal() + b.getVal())/2;
        }
        else
        {
            return (double) 60;
        }
    }
    @Override
    public Double getEcartType()
    {
        if (a.getVal() <= b.getVal())
        {
            return (b.getVal() - a.getVal())/Math.sqrt(12);
        }
        else
        {
            return (double) 0;
        }
    }

}
