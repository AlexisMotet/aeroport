package canvas;

import aeroport.elementsgraphiques.AeroportGraphique;

public class CanvasAeroport extends ResizableCanvas {
    private final AeroportGraphique aeroportGraphique;
    public CanvasAeroport(AeroportGraphique aeroportGraphique) {
        this.aeroportGraphique = aeroportGraphique;
    }

    @Override
    public void resize(double largeur, double hauteur) {
        setWidth(largeur);
        setHeight(hauteur);
        aeroportGraphique.peindre(getGraphicsContext2D(), 0, 0, largeur, hauteur);
    }
}
