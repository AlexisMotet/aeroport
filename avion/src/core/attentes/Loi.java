package core.attentes;


import java.util.ArrayList;

public abstract class Loi {

    public abstract String getNom();
    public abstract ArrayList<Parametre> getParametres();

    public abstract long next();

    public abstract Double getEsperance();

    public abstract Double getEcartType();

    public static String[] nomsLois = {
            "Loi uniforme",
            "Loi triangulaire",
            "Loi exponentielle",
            "Loi gaussienne"
    };
}
