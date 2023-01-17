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

    public static String chemin = "file:ihm\\ressources\\img\\taxiway.png";
    private static Image image;
    private final Boolean TW1;
    private final HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();

    public HashMap<Avion.eEtat, Point> getMapEtats() {
        return mapEtats;
    }

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
        if (TW1) mapEtats.put(Avion.eEtat.ROULEMENT_ARRIVEE, new Point(x0 + w/2,
                y0 + h - h/5));
        else mapEtats.put(Avion.eEtat.ROULEMENT_DEPART, new Point(x0 + w/2, y0 + h/5));
    }

    @Override
    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne) {
        return mapEtats.get(etat);
    }
}
