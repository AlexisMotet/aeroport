package aeroport;

import core.Avion;
import core.Terminal;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class TerminalGraphique implements ElementGraphique{

    static String chemin = "file:ihm\\ressources\\img\\terminal.png";
    static Image image;
    Terminal terminal;
    HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();
    HashMap<Integer, GateGraphique> mapEmplacements = new HashMap<>();
    TaxiwayGraphique TW1;
    TaxiwayGraphique TW2;

    public TerminalGraphique(Terminal terminal) {
        this.terminal = terminal;
    }
    public static void chargerImage()
    {
        image = new Image(chemin);
    }

    @Override
    public void peindre(GraphicsContext gc, int x0, int y0, int w, int h)
    {
        int hauteurTaxiway = h/2;
        int x0Sauv = x0;
        int y0Sauv = y0;
        y0 += hauteurTaxiway;
        int hauteurTerminal = (h - hauteurTaxiway) * 2/3;
        int largeurTerminal = w/2;
        while (largeurTerminal % 7 != 0)
        {
            largeurTerminal ++;
        }
        while (hauteurTerminal % 7 != 0)
        {
            hauteurTerminal ++;
        }
        int espace = (w - largeurTerminal)/2;
        gc.drawImage(image, x0 + espace, y0, largeurTerminal, hauteurTerminal);
        x0 += espace;
        int largeurEmplacement = largeurTerminal/7;
        int hauteurEmplacement = hauteurTerminal/7;
        x0 += largeurEmplacement;
        y0 += hauteurEmplacement;
        for (Terminal.Gate emplacement : terminal.getEmplacements())
        {
            GateGraphique emplacementGraphique = new GateGraphique(emplacement);
            emplacementGraphique.peindre(gc, x0, y0, largeurEmplacement, hauteurEmplacement);
            mapEmplacements.put(emplacement.hashCode(), emplacementGraphique);
            if (x0 + 3 * largeurEmplacement > (x0Sauv + espace + largeurTerminal))
            {
                x0 = x0Sauv + espace + largeurEmplacement;
                y0 += 2 * hauteurEmplacement;
            }
            else
            {
                x0 += 2 * largeurEmplacement;
            }
        }
        x0 = x0Sauv;
        y0 = y0Sauv;
        int largeurTaxiway = w/10;

        TW1 = new TaxiwayGraphique(true);
        TW2 = new TaxiwayGraphique(false);

        TW1.peindre(gc, x0 + espace + largeurTaxiway, y0, largeurTaxiway, hauteurTaxiway);
        TW2.peindre(gc, x0 + w - (espace + 2 * largeurTaxiway), y0, largeurTaxiway, hauteurTaxiway);

        mapEtats.put(Avion.eEtat.ATTERISSAGE, new Point(x0 + espace + largeurTaxiway + largeurTaxiway/2 - AvionGraphique.largeur/2,
                y0 - AvionGraphique.hauteur/2));
    }

    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne)
    {
        if (mapEtats.containsKey(etat)) return mapEtats.get(etat);
        else if (TW1.mapEtats.containsKey(etat)) return TW1.obtenirPoint(etat, consigne);
        else if (TW2.mapEtats.containsKey(etat)) return TW2.obtenirPoint(etat, consigne);
        return mapEmplacements.get(consigne.emplacement).obtenirPoint(etat, consigne);
    }

    public static class TaxiwayGraphique implements ElementGraphique {

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

    public static class GateGraphique implements ElementGraphique {
        Terminal.Gate emplacement;
        HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();
        public GateGraphique(Terminal.Gate emplacement) {
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

}
