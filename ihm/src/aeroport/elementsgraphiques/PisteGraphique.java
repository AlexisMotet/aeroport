package aeroport.elementsgraphiques;

import aeroport.AvionGraphique;
import aeroport.Point;
import canvas.CanvasAvion;
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
    public void peindre(GraphicsContext gc, double x0, double y0, double w, double h)
    {
        double hauteurPiste = h/3;
        gc.drawImage(image, x0, y0, w, hauteurPiste);
        Point point = new Point(x0 + CanvasAvion.largeurAvion,
                y0 + hauteurPiste/2 - CanvasAvion.hauteurAvion/2);
        mapEtats.put(Avion.eEtat.APPROCHE, point);
        mapEtats.put(Avion.eEtat.DECOLLAGE, point);
        double hauteurTerminal = h - hauteurPiste;
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