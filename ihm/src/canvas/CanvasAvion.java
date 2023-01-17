package canvas;

public class CanvasAvion extends ResizableCanvas {
    static public double largeurAvion;
    static public double hauteurAvion;

    public CanvasAvion() {
        resize(getWidth(), getHeight());
    }

    @Override
    public void resize(double largeur, double hauteur) {
        setWidth(largeur);
        setHeight(hauteur);
        double min = Math.min(largeur, hauteur);
        largeurAvion = min/10;
        hauteurAvion = largeurAvion;
    }
}
