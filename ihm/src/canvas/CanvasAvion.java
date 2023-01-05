package canvas;

public class CanvasAvion extends ResizableCanvas {
    static public double largeurAvion;
    static public double hauteurAvion;

    @Override
    public void resize(double largeur, double hauteur) {
        setWidth(largeur);
        setHeight(hauteur);
        double min = Math.min(largeur, hauteur);
        largeurAvion = min/10;
        hauteurAvion = largeurAvion;
        System.out.println(largeurAvion);
    }

    public void effacer()
    {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }
}
