package aeroport;

import core.Avion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piste {
    int v0, v1, v2, v3;
    int largeur, longueur;
    public Piste(int v0, int largeur , int longueur) {
        this.v0 = v0;
        this.v1 = 0;
        this.v2 = v0 + largeur;
        this.v3 = longueur;
        this.largeur = largeur;
        this.longueur = longueur;
    }

    public void peindrePiste(GraphicsContext gc, Color couleur)
    {
        gc.setFill(couleur);
        gc.fillRect(v0, v1, v2, v3);
    }

    public void peindreAvion(GraphicsContext gc, Color couleur, Avion.enumPosition pos)
    {
        gc.setFill(couleur);
        switch(pos)
        {
            case DEBUT_PISTE :
                gc.fillRect(v0 + (float) largeur/2 - 10, 0, v0 + (float) largeur/2 + 10, 20);
            case MILIEU_PISTE :
                gc.fillRect(v0 + (float) largeur/2 - 10, (float) longueur/2, v0 + (float) largeur/2 + 10, (float) longueur/2 + 20);
            case FIN_PISTE :
                gc.fillRect(v0 + (float) largeur/2 - 10, longueur - 20, v0 + (float) largeur/2 + 10, longueur);
        }
    }
}
