package aeroport.elementsgraphiques;

import aeroport.Point;
import core.Avion;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;

public interface ElementGraphique {

    void peindre(GraphicsContext gc, int x0, int y0, int w, int h);
    Point obtenirPoint(Avion.eEtat etat, Consigne consigne);
}
