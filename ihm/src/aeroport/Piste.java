package aeroport;

import core.Avion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piste {
    final int x0;
    final int largeur, longueur;
    public Piste(int x0, int largeur , int longueur) {
        this.x0 = x0;
        this.largeur = largeur;
        this.longueur = longueur;
    }

    public void peindrePiste(GraphicsContext gc, Color couleur)
    {
        gc.setFill(couleur);
        gc.fillRect(x0, 0, largeur, longueur);
    }
}
