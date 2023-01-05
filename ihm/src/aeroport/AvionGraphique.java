package aeroport;

import canvas.CanvasAvion;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import core.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
            "Bipolar Plane\\Type_7\\Animation\\",
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

    String type;
    static HashMap<String, Image> mapImages = new HashMap<>();
    private int x = 0;
    private int y = 0;

    ArrayList<Image> images = new ArrayList<>();

    private int r = 0;

    public AvionGraphique() {
        Random rand = new Random();
        int rnd = rand.nextInt(types.length);
        type = chemin + types[rnd];
        ImageView imageView = new ImageView(mapImages.get(type + "1.png"));
        SnapshotParameters parametres = new SnapshotParameters();
        parametres.setFill(Color.TRANSPARENT);
        for (int i = 0; i < 4; i++)
        {
            images.add(imageView.snapshot(parametres, null));
            imageView.setRotate(imageView.getRotate() + 90);
        }
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

    public Boolean avancerEtPeindreAvion(GraphicsContext gc, Point point)
    {
        int vitesse = 1;
        if (point == null) {
            return true;
        }
        if (point.getX() - x > 0)
        {
            x += vitesse;
            r = 1;
        }
        else if (y - point.getY() > 0)
        {
            y -= vitesse;
            r = 0;
        }
        else if (x - point.getX() > 0)
        {
            x -= vitesse;
            r = 3;
        }
        else if (point.getY() - y > 0)
        {
            y += vitesse;
            r = 2;
        }
        gc.drawImage(images.get(r),  x, y,
                CanvasAvion.largeurAvion, CanvasAvion.hauteurAvion);
        return (x == point.getX() && y == point.getY());
    }

    public static void main(String[] args)
    {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

    }

}
