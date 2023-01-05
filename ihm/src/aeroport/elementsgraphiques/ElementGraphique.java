package aeroport.elementsgraphiques;

import aeroport.Point;
import core.Avion;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;

public interface ElementGraphique {

    void peindre(GraphicsContext gc, double x0, double y0, double w, double h);
    Point obtenirPoint(Avion.eEtat etat, Consigne consigne);
}
