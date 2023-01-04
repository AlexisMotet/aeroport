package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import core.Avion;
import core.elements.Emplacement;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class EmplacementGraphique implements ElementGraphique {
    Emplacement emplacement;
    HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();
    public EmplacementGraphique(Emplacement emplacement) {
        this.emplacement = emplacement;
    }

    @Override
    public void peindre(GraphicsContext gc, int x0, int y0, int w, int h)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(x0, y0, w, h);
        mapEtats.put(Avion.eEtat.DECHARGEMENT_PASSAGERS, new Point(x0 + w/2 - AvionGraphique.largeur/2,
                y0 + h/2 - AvionGraphique.hauteur/2));
    }

    @Override
    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne)
    {
        if (mapEtats.containsKey(etat)) {
            return mapEtats.get(etat);
        }
        return null;
    }
}
