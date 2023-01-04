package core.attente;

import java.util.HashMap;

public class Uniforme extends Loi {
    private double a, b;

    public Uniforme(double a, double b) {
        nom = "Uniforme";
        this.a = a;
        this.b = b;
    }
    private final HashMap<String, Double> parametres = new HashMap<>(){{
        put("a", a);
        put("b", b);
    }};

    @Override
    public Double next(){
        return getRandom().nextUniform(a, b);
    }
    @Override
    public HashMap<String, Double> getParametres() {
        return parametres;
    }
}
