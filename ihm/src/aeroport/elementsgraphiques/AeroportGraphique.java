package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import canvas.CanvasAvion;
import core.Avion;
import core.elements.Aeroport;
import core.elements.Piste;
import core.elements.Terminal;
import core.protocole.Consigne;
import enstabretagne.base.math.MoreRandom;
import javafx.scene.AccessibleAction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Objects;

public class AeroportGraphique implements ElementGraphique {
    public static String chemin = "file:ihm\\ressources\\img\\herbe.png";
    private static Image image;
    private final Aeroport aeroport;
    private final HashMap<Integer, PisteGraphique> mapPistes = new HashMap<>();

    private double rayonTourDeControle;
    private double r = 0;
    private double xTourDeControle;
    private double yTourDeControle;
    private double xCiel;
    private double y0Ciel;
    private final HashMap<Integer, Point> mapPointsDuCiel = new HashMap<>();

    public HashMap<Integer, Point> getMapPointsDuCiel() {
        return mapPointsDuCiel;
    }
    private double hauteurCiel;

    private static final MoreRandom random = new MoreRandom();

    private final HashMap<PisteGraphique, Point> mapConsignePistes  = new HashMap<>();

    public AeroportGraphique(Aeroport aeroport) {
        this.aeroport = aeroport;
    }

    public static void chargerImage()
    {
        image = new Image(chemin);
    }
    private void dessinerTourDeControle(GraphicsContext gc, double y0, double w, double h)
    {
        double rayon = h/10;
        rayonTourDeControle = rayon;
        xTourDeControle = w - 3 * rayon;
        yTourDeControle = y0 + h/2;
        gc.setFill(Color.WHITE);
        gc.fillOval(xTourDeControle - rayon, yTourDeControle - rayon, 2*rayon, 2*rayon);
        rayon = h/12;
        gc.setFill(Color.RED);
        gc.fillOval(xTourDeControle - rayon, yTourDeControle - rayon, 2*rayon, 2*rayon);
        rayon = h/20;
        gc.setFill(Color.WHITE);
        gc.fillOval(xTourDeControle - rayon, yTourDeControle - rayon, 2*rayon, 2*rayon);
    }

    public void animationTourDeControle(GraphicsContext gc)
    {
        double rayon = rayonTourDeControle + r;
        gc.setFill(Color.BLACK);
        gc.strokeOval(xTourDeControle - rayon, yTourDeControle - rayon, 2*rayon, 2*rayon);
        r += 0.2;
        if (rayonTourDeControle + r > 2 * rayonTourDeControle) r = 0;
    }

    @Override
    public void peindre(GraphicsContext gc, double x0, double y0, double w, double h) {
        double hauteurBandeHerbe = h/4;
        gc.drawImage(image, x0, y0, w, h);
        dessinerTourDeControle(gc, y0, w, hauteurBandeHerbe);
        xCiel = w + 3 * CanvasAvion.largeurAvion;
        y0Ciel = y0;
        genererMapDePointsDuCiel();
        hauteurCiel = h;
        y0 += hauteurBandeHerbe;
        double hauteurAeroport = h - hauteurBandeHerbe;
        double hauteurPisteEtTerminaux = hauteurAeroport/3;
        for (Piste piste : aeroport.getPistes())
        {
            PisteGraphique pisteGraphique = new PisteGraphique(piste);
            mapPistes.put(piste.hashCode(), pisteGraphique);
            pisteGraphique.peindre(gc, x0, y0, w, hauteurPisteEtTerminaux);
            mapConsignePistes.put(pisteGraphique, new Point(x0 - 3 * CanvasAvion.largeurAvion,
                    y0 + hauteurPisteEtTerminaux/12));
            y0 += hauteurPisteEtTerminaux;
        }
    }

    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne){
        if (etat == Avion.eEtat.CIEL || etat == Avion.eEtat.CIEL_DEPART) return null;
        else if (etat == Avion.eEtat.CIEL_CONSIGNE_ARRIVEE || etat == Avion.eEtat.PISTE_CONSIGNE) {
            return mapConsignePistes.get(mapPistes.get(consigne.getPiste()));
        }
        PisteGraphique piste = mapPistes.get(consigne.getPiste());
        return piste.obtenirPoint(etat, consigne);
    }

    public int genererIdPointDuCiel()
    {
        return (int) random.nextUniform(0, 40);
    }

    public void genererMapDePointsDuCiel()
    {
        for (int i = 0; i < 40; i++) mapPointsDuCiel.put(i, genererPointDuCiel());
    }

    public Point genererPointDuCiel()
    {
        double offset = random.nextUniform(-hauteurCiel/10, hauteurCiel/10);
        double pileOuFace = random.nextUniform();
        double y;
        if (pileOuFace < 0.5) y = y0Ciel + offset;
        else y = y0Ciel + hauteurCiel + offset;
        return new Point(xCiel, y);
    }
}

