package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import canvas.CanvasAvion;
import core.Avion;
import core.elements.Emplacement;
import core.elements.Terminal;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class TerminalGraphique implements ElementGraphique {

    public static String chemin = "file:ihm\\ressources\\img\\terminal.png";
    private static Image image;
    private final Terminal terminal;
    private final HashMap<Avion.eEtat, Point> mapEtats = new HashMap<>();
    private final HashMap<Integer, EmplacementGraphique> mapEmplacements = new HashMap<>();
    private TaxiwayGraphique TW1;
    private TaxiwayGraphique TW2;

    public TerminalGraphique(Terminal terminal) {
        this.terminal = terminal;
    }
    public static void chargerImage()
    {
        image = new Image(chemin);
    }

    @Override
    public void peindre(GraphicsContext gc, double x0, double y0, double w, double h)
    {
        double hauteurTaxiway = h/2;
        double x0Sauv = x0;
        double y0Sauv = y0;
        y0 += hauteurTaxiway;
        double hauteurTerminal = (h - hauteurTaxiway) * 2/3;
        double largeurTerminal = w/2;
        while ((int)largeurTerminal % 7 != 0)
        {
            largeurTerminal ++;
        }
        while ((int)hauteurTerminal % 7 != 0)
        {
            hauteurTerminal ++;
        }
        double espace = (w - largeurTerminal)/2;
        gc.drawImage(image, x0 + espace, y0, largeurTerminal, hauteurTerminal);
        x0 += espace;
        double largeurEmplacement = largeurTerminal/7;
        double hauteurEmplacement = hauteurTerminal/7;
        x0 += largeurEmplacement;
        y0 += hauteurEmplacement;
        for (Emplacement emplacement : terminal.getEmplacements())
        {
            EmplacementGraphique emplacementGraphique = new EmplacementGraphique(emplacement);
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
        double largeurTaxiway = w/10;

        TW1 = new TaxiwayGraphique(true);
        TW2 = new TaxiwayGraphique(false);

        TW1.peindre(gc, x0 + espace + largeurTaxiway, y0, largeurTaxiway, hauteurTaxiway);
        TW2.peindre(gc, x0 + w - (espace + 2 * largeurTaxiway), y0, largeurTaxiway, hauteurTaxiway);

        Point point = new Point(x0 + espace + largeurTaxiway + largeurTaxiway/2,
                y0);

        mapEtats.put(Avion.eEtat.ATTERISSAGE, point);
    }

    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne)
    {
        if (mapEtats.containsKey(etat)) return mapEtats.get(etat);
        else if (TW1.getMapEtats().containsKey(etat)) return TW1.obtenirPoint(etat, consigne);
        else if (TW2.getMapEtats().containsKey(etat)) return TW2.obtenirPoint(etat, consigne);
        return mapEmplacements.get(consigne.getEmplacement()).obtenirPoint(etat, consigne);
    }


}
