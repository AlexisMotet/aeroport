package aeroport;

import core.Aeroport;
import core.Avion;
import core.Terminal;
import core.protocole.Consigne;
import javafx.scene.canvas.GraphicsContext;
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
    public void peindre(GraphicsContext gc, int x0, int y0, int w, int h) {
        int hauteurCiel = h/6;
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(x0, y0, w, hauteurCiel);
        y0 += hauteurCiel;
        gc.drawImage(image, x0, y0, w, h);
        int hauteurAeroport = h - h/6;
        for (Aeroport.Piste piste : aeroport.getPistes())
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

    public static class PisteGraphique implements ElementGraphique{
        static String chemin = "file:ihm\\ressources\\img\\piste.png";
        static Image image;
        Aeroport.Piste piste;
        private final HashMap<Integer, TerminalGraphique> mapTerminaux = new HashMap<>();
        HashMap<Avion.eEtat, Point> mapEtats;

        public PisteGraphique(Aeroport.Piste piste)
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

}
