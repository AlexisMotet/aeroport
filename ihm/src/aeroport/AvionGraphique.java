package aeroport;

import canvas.CanvasAvion;
import core.Avion;
import core.elements.Aeroport;
import core.protocole.Consigne;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class AvionGraphique {

    static String chemin = "file:ihm\\ressources\\TopDown_Planes_Game_Assets_without_source_by_unlucky_studio\\Without Source files -Game Assets\\";
    static String[] types = {
            "B-17\\Type-1\\Animation\\",
            "B-17\\Type-2\\Animated\\",
            "B-17\\Type-3\\Animated\\",
            "B-17\\Type-4\\Animated\\",
            "BF-109E\\Type-1\\Animation\\",
            "BF-109E\\Type-2\\Animation\\",
            "BF-109E\\Type-3\\Animation\\",
            "BF-109E\\Type-4\\Animated\\",
            "Bipolar Plane\\Type_1\\ANimated\\",
            "Bipolar Plane\\Type_2\\Animation\\",
            "Bipolar Plane\\Type_3\\Animation\\",
            "Bipolar Plane\\Type_4\\Animation\\",
            "Bipolar Plane\\Type_5\\Animation\\",
            "Bipolar Plane\\Type_6\\Animation\\",
            "Bipolar Plane\\Type_7\\Animated\\",
            "Blenheim\\Type_1\\Animation\\",
            "Blenheim\\Type_2\\Animation\\",
            "Hawker Tempest MKII\\Type_1\\Animated\\",
            "Hawker Tempest MKII\\Type_2\\Animation\\",
            "Hawker Tempest MKII\\Type_3\\Animation\\",
            "JU-87B2\\Type_1\\Animation\\",
            "JU-87B2\\Type_2\\Animation\\",
            "JU-87B2\\Type_3\\Animation\\",
            "TBM3\\Type_1\\Animation\\",
            "TBM3\\Type_2\\Animation\\",
            "TBM3\\Type_3\\Animation\\"
    };

    static HashMap<String, Image> mapImages = new HashMap<>();

    static int vitesse = 10;
    private int r = 0;
    private int rCiel;
    private int x;
    private int y;
    int idPointDuCiel;
    private Double m;
    private Double b;
    SnapshotParameters parametres;
    private final ImageView imageView;
    private Image image = null;
    private double dernierR = -1;

    public AvionGraphique(Point pointDuCiel, int idPointDuCiel) {
        int rnd = (int) Aeroport.getRandom().nextUniform(0, types.length);
        String type = chemin + types[rnd];
        imageView = new ImageView(mapImages.get(type + "1.png"));
        parametres = new SnapshotParameters();
        parametres.setFill(Color.TRANSPARENT);
        imageView.setRotate(90);
        x = pointDuCiel.getX();
        y = pointDuCiel.getY();
        this.idPointDuCiel = idPointDuCiel;
    }

    private void setImage(int r)
    {
        imageView.setRotate(90 + r);
        image = imageView.snapshot(parametres, null);
    }

    public static void chargerImages()
    {
        for (String nom : types)
        {
            mapImages.put(chemin + nom + "1.png", new Image(chemin + nom + "1.png"));
            mapImages.put(chemin + nom + "2.png", new Image(chemin + nom + "2.png"));
            mapImages.put(chemin + nom + "3.png", new Image(chemin + nom + "3.png"));
        }
    }

    public Boolean avancerEtPeindreAvion(GraphicsContext gc, HashMap<Integer, Point> mapPointsDuCiel,
                                         Point futurPoint, Avion.eEtat etat,
                                         Consigne consigne)
    {
        boolean peindre = true;
        if (etat == Avion.eEtat.CIEL) return true;
        else if (etat == Avion.eEtat.CIEL_CONSIGNE_ARRIVEE || etat == Avion.eEtat.CIEL_DEPART) {
            if (etat == Avion.eEtat.CIEL_DEPART){
                futurPoint = mapPointsDuCiel.get(idPointDuCiel);
            }
            if (m == null)
            {
                m = (double) (futurPoint.getY() - y) / (futurPoint.getX() - x);
                b = futurPoint.getY() - m * futurPoint.getX();
                rCiel = angleDepuisPoints(new Point(x, y), futurPoint);
            }
            if (futurPoint.getX() - x > 0)
            {
                x += vitesse;
                r = rCiel + 180;
                while (r >= 360) r-= 360;
                y = (int) (m * x + b);
            }
            else if (futurPoint.getX() - x < 0)
            {
                x -= vitesse;
                r = rCiel;
                y = (int) (m * x + b);
            }
            else if (y - futurPoint.getY() > 0)
            {
                y -= vitesse;
                r = 270;
                peindre = false;
            }
            else if (futurPoint.getY() - y > 0)
            {
                y += vitesse;
                r = 90;
                peindre = false;
            }
        }
        else {
            if (futurPoint.getX() - x > 0)
            {
                x += vitesse;
                r = 0;
            }
            else if (y - futurPoint.getY() > 0)
            {
                y -= vitesse;
                r = 270;
            }
            else if (x - futurPoint.getX() > 0)
            {
                x -= vitesse;
                r = 180;
            }
            else if (futurPoint.getY() - y > 0)
            {
                y += vitesse;
                r = 90;
            }
        }

        if (Math.abs(x - futurPoint.getX()) <= vitesse) x = futurPoint.getX();
        if (Math.abs(y - futurPoint.getY()) <= vitesse) y = futurPoint.getY();

        if (dernierR == -1 || dernierR != r)
        {
            setImage(r);
            dernierR = r;
        }
        if (peindre){
            if (etat == Avion.eEtat.CIEL_CONSIGNE_ARRIVEE || etat == Avion.eEtat.CIEL_DEPART){
                gc.drawImage(image,
                        x - ((double)3/2) * CanvasAvion.largeurAvion,
                        y - ((double)3/2) * CanvasAvion.hauteurAvion,
                        3 * CanvasAvion.largeurAvion,
                        3 * CanvasAvion.hauteurAvion);
            }
            else{
                gc.strokeText(Integer.toString(consigne.getNumero()), x + CanvasAvion.largeurAvion/2,
                        y + CanvasAvion.hauteurAvion/2);

                gc.drawImage(image,
                        x - CanvasAvion.largeurAvion/2,
                        y - CanvasAvion.hauteurAvion/2,
                        CanvasAvion.largeurAvion,
                        CanvasAvion.hauteurAvion);
            }
        }


        if (x == futurPoint.getX() && y == futurPoint.getY())
        {
            if (etat == Avion.eEtat.CIEL_DEPART)
                m = null;
            return true;
        }
        return false;
    }

    private int angleDepuisPoints(Point ancienPoint, Point nouveauPoint)
    {
        double vx = nouveauPoint.getX() - ancienPoint.getX();
        double vy = nouveauPoint.getY() - ancienPoint.getY();
        int theta = (int) Math.abs(Math.toDegrees(Math.atan(vy/vx)));
        if (vx >= 0 && vy >= 0) return theta;
        else if (vx < 0 && vy >= 0) return 180 - theta;
        else if (vx < 0) return 180 + theta;
        else {
            if (theta == 0) return 0;
            else return 360 - theta;
        }
    }
}
