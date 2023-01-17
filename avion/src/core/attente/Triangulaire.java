package core.attente;

import java.util.ArrayList;

public class Triangulaire extends Loi {
    private final Parametre a = new Parametre("a", 0, 10);
    private final Parametre b = new Parametre("b", 0, 10);
    private final Parametre c = new Parametre("c", 0, 10);
    public static double A = 2;
    public static double B = 5;
    public static double C = 7;

    public Triangulaire() {
        this.a.setVal(A);
        this.b.setVal(B);
        this.c.setVal(C);
    }
    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(a);
        add(b);
        add(c);
    }};

    @Override
    public long next(){
        if (a.getVal() < b.getVal() && a.getVal() <= c.getVal() && c.getVal() <= b.getVal())
        {
            return (long) getRandom().nextTriangle(a.getVal(), b.getVal(), c.getVal());
        }
        else
        {
            return 99;
        }
    }

    @Override
    public eLoi getNom() {
        return eLoi.LOI_TRIANGULAIRE;
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }

    public Double getEsperance()
    {
        if (a.getVal() < b.getVal() && a.getVal() <= c.getVal() && c.getVal() <= b.getVal())
        {
            return (a.getVal() + b.getVal() + c.getVal())/3;
        }
        else
        {
            return (double) 99;
        }
    }
}
