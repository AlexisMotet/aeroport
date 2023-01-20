package core.attente;

import java.util.ArrayList;

public class Triangulaire extends Loi {
    private final Parametre a = new Parametre("a", 0, 60);
    private final Parametre b = new Parametre("b", 0, 60);
    private final Parametre c = new Parametre("c", 0, 60);
    public static double A = 2;
    public static double B = 5;
    public static double C = 7;

    public Triangulaire() {
        a.setVal(A);
        b.setVal(B);
        c.setVal(C);
    }

    public Triangulaire(double a, double b, double c) {
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
    public long next(){
        if (a.getVal() < b.getVal() && a.getVal() <= c.getVal() && c.getVal() <= b.getVal())
        {
            return (long) getRandom().nextTriangle(a.getVal(), b.getVal(), c.getVal());
        }
        else
        {
            return 60;
        }
    }

    @Override
    public String getNom() {
        return "Loi triangulaire";
    }
    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }
    @Override
    public Double getEsperance()
    {
        if (a.getVal() < b.getVal() && a.getVal() <= c.getVal() && c.getVal() <= b.getVal())
        {
            return (a.getVal() + b.getVal() + c.getVal())/3;
        }
        else
        {
            return (double) 60;
        }
    }
    @Override
    public Double getEcartType()
    {
        if (a.getVal() < b.getVal() && a.getVal() <= c.getVal() && c.getVal() <= b.getVal())
        {
            return Math.sqrt((Math.pow(a.getVal(),2) + Math.pow(b.getVal(), 2) + Math.pow(c.getVal(), 2) -
                    a.getVal()*b.getVal() -a.getVal()*c.getVal() - b.getVal() * c.getVal())/18);
        }
        else
        {
            return (double) 0;
        }
    }
}
