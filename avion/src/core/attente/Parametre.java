package core.attente;

public class Parametre {

    public String getNom() {
        return nom;
    }
    private final String nom;
    private final double min;
    private final double max;

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    private double val;

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public Parametre(String nom, double min, double max) {
        assert min < max;
        this.nom = nom;
        this.min = min;
        this.max = max;
    }
}
