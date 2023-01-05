package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import canvas.CanvasAvion;
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
    public void peindre(GraphicsContext gc, double x0, double y0, double w, double h) {
        gc.drawImage(image, x0, y0, w, h);
        Point point = new Point(x0 + w/2 - CanvasAvion.largeurAvion/2, y0 + h - CanvasAvion.hauteurAvion);
        if (TW1) mapEtats.put(Avion.eEtat.ROULEMENT_ARRIVEE, point);
        else mapEtats.put(Avion.eEtat.ROULEMENT_DEPART, point);
    }

    @Override
    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne) {
        return mapEtats.get(etat);
    }
}
