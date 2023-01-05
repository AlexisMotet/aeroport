package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import core.Avion;
import core.elements.Aeroport;
import core.elements.Piste;
import core.elements.Terminal;
import core.protocole.Consigne;
import javafx.scene.AccessibleAction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Objects;

public class AeroportGraphique implements ElementGraphique {

    static String chemin = "file:ihm\\ressources\\img\\herbe.png";
    static Image image;
    private final Aeroport aeroport;
    private final HashMap<Integer, PisteGraphique> mapPistes = new HashMap<>();
    public AeroportGraphique(Aeroport aeroport) {
        this.aeroport = aeroport;
    }

    public static void chargerImage()
    {
        image = new Image(chemin);
    }

    @Override
    public void peindre(GraphicsContext gc, double x0, double y0, double w, double h) {
        double hauteurCiel = h/6;
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(x0, y0, w, hauteurCiel);
        y0 += hauteurCiel;
        gc.drawImage(image, x0, y0, w, h);
        double hauteurAeroport = h - h/6;
        for (Piste piste : aeroport.getPistes())
        {
            PisteGraphique pisteGraphique = new PisteGraphique(piste);
            mapPistes.put(piste.hashCode(), pisteGraphique);
            pisteGraphique.peindre(gc, x0, y0, w, hauteurAeroport/3);
            y0 += hauteurAeroport/3;
        }
    }

    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne){
        if (Objects.isNull(consigne)) return null;
        PisteGraphique piste = mapPistes.get(consigne.piste);
        return piste.obtenirPoint(etat, consigne);
    }
}

