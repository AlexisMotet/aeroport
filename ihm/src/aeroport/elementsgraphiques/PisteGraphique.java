package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import core.Avion;
import core.elements.Piste;
import core.elements.Terminal;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;

public class PisteGraphique implements ElementGraphique{
    static String chemin = "file:ihm\\ressources\\img\\piste.png";
    static Image image;
    Piste piste;
    private final HashMap<Integer, TerminalGraphique> mapTerminaux = new HashMap<>();
    HashMap<Avion.eEtat, Point> mapEtats;

    public PisteGraphique(Piste piste)
    {
        mapEtats = new HashMap<>();
        this.piste = piste;
    }
    public static void chargerImage()
    {
        image = new Image(chemin);
    }
    @Override
    public void peindre(GraphicsContext gc, int x0, int y0, int w, int h)
    {
        int hauteurPiste = h/3;
        gc.drawImage(image, x0, y0, w, hauteurPiste);
        mapEtats.put(Avion.eEtat.APPROCHE, new Point(x0 + AvionGraphique.largeur,
                y0 + hauteurPiste/2 - AvionGraphique.hauteur/2));
        mapEtats.put(Avion.eEtat.DECOLLAGE, new Point(x0 + AvionGraphique.largeur,
                y0 + hauteurPiste/2 - AvionGraphique.hauteur/2));
        int hauteurTerminal = h - hauteurPiste;
        for (Terminal terminal : piste.getTerminaux())
        {
            TerminalGraphique terminalGraphique = new TerminalGraphique(terminal);
            mapTerminaux.put(terminal.hashCode(), terminalGraphique);
            terminalGraphique.peindre(gc, x0, y0 + hauteurPiste,
                    w/3, hauteurTerminal);
            x0 += w/3;
        }
    }
    public Point obtenirPoint(Avion.eEtat etat, Consigne consigne){
        if (mapEtats.containsKey(etat))
            return mapEtats.get(etat);
        TerminalGraphique terminal = mapTerminaux.get(consigne.terminal);
        return terminal.obtenirPoint(etat, consigne);
    }
}