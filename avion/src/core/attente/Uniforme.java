package core.attente;

import java.util.ArrayList;
import java.util.HashMap;

public class Uniforme extends Loi {
    private final Parametre a = new Parametre("a", 0, 10);
    private final Parametre b = new Parametre("b", 0, 10);

    public Uniforme(double a, double b) {
        this.a.setVal(a);
        this.b.setVal(b);
    }
    private final ArrayList<Parametre> parametres = new ArrayList<>(){{
        add(a);
        add(b);
    }};

    @Override
    public Double next(){
        return getRandom().nextUniform(a.getVal(), b.getVal());
    }

    @Override
    public String getNom() {
        return "Uniforme";
    }

    @Override
    public ArrayList<Parametre> getParametres() {
        return parametres;
    }
    public Double getEsperance()
    {
        if (a.getVal() <= b.getVal())
        {
            return null;
        }
        else
        {
            return (a.getVal() + b.getVal())/2;
        }
    }

}
