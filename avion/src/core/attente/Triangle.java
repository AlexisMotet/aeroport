package core.attente;

import enstabretagne.base.math.MoreRandom;

import java.util.HashMap;

public class Triangle extends Loi {
    private double a, b, c;

    public Triangle(double a, double b, double c) {
        nom = "Triangle";
        this.a = a;
        this.b = b;
        this.c = c;
    }
    private final HashMap<String, Double> parametres = new HashMap<>(){{
        put("a", a);
        put("b", b);
        put("c", c);
    }};

    @Override
    public Double next(){
        return getRandom().nextTriangle(a, b, c);
    }
    @Override
    public HashMap<String, Double> getParametres() {
        return parametres;
    }
}
