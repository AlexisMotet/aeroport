package core.attente;

import java.util.ArrayList;
import java.util.HashMap;

public class Uniforme extends Loi {
    private final Parametre a = new Parametre("a", 0, 10);
    private final Parametre b = new Parametre("b", 0, 10);

    public static double A = 2;
    public static double B = 5;

    public Uniforme() {
        this.a.setVal(A);
        this.b.setVal(B);
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
            return 99;
        }
    }

    @Override
    public eLoi getNom() {
        return eLoi.UNIFORME;
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }
    public Double getEsperance()
    {
        if (a.getVal() < b.getVal())
        {
            return (a.getVal() + b.getVal())/2;
        }
        else
        {
            return (double) 99;
        }
    }

}
