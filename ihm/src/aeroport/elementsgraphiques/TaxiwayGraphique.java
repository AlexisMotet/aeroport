package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import core.Avion;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;

public class TaxiwayGraphique implements ElementGraphique {

    static String chemin = "file:ihm\\ressources\\img\\taxiway.png";
    static Image image;
    Boolean TW1;
    HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();
    public TaxiwayGraphique(Boolean TW1) {
        this.TW1 = TW1;
    }

    public static void chargerImage()
    {
        image = new Image(chemin);
    }

    @Override
    public void peindre(GraphicsContext gc, int x0, int y0, int w, int h) {
        gc.drawImage(image, x0, y0, w, h);
        if (TW1) mapEtats.put(Avion.eEtat.ROULEMENT_ARRIVEE, new Point(x0 + w/2 - AvionGraphique.largeur/2, y0 + h - AvionGraphique.hauteur));
        else mapEtats.put(Avion.eEtat.ROULEMENT_DEPART, new Point(x0 + w/2 - AvionGraphique.largeur/2, y0 + AvionGraphique.hauteur));
    }

    @Override
    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne) {
        return mapEtats.get(etat);
    }
}
