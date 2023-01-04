package aeroport;

import aeroport.elementsgraphiques.AeroportGraphique;
import aeroport.elementsgraphiques.PisteGraphique;
import aeroport.elementsgraphiques.TaxiwayGraphique;
import aeroport.elementsgraphiques.TerminalGraphique;
import core.Avion;
import core.attente.Loi;
import core.attente.Parametre;
import core.elements.Aeroport;
import core.elements.Piste;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.control.skin.SpinnerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class App extends Application {

    SimuEngine eng;
    Canvas canvas_background;
    Canvas canvas_avion;
    GraphicsContext gc_bg;
    GraphicsContext gc_av;
    int hauteur = 600;
    int largeur = 500;

    AeroportGraphique aeroportGraphique;

    HashMap<Avion, AvionGraphique> mapAvions = new HashMap<>();
    VBox vBox;

    HBox hBoxHeure;
    Label labelHeure;

    HBox hBoxEvenement;
    Label labelEvenement;

    ArrayList<HBox> hBoxActions = new ArrayList<>();
    ArrayList<Label> labelActions = new ArrayList<>();
    HashMap<Avion, InfoAvion> mapInfo;

    @Override
    public void start(Stage stage) {
        HBox hBox = new HBox();

        vBox = new VBox();
        mapInfo = new HashMap<>();

        labelHeure = new Label();
        hBoxHeure = new HBox();
        hBoxHeure.getChildren().add(labelHeure);

        labelEvenement = new Label();
        hBoxEvenement = new HBox();
        hBoxEvenement.getChildren().add(labelEvenement);

        vBox.getChildren().add(hBoxHeure);
        vBox.getChildren().add(hBoxEvenement);

        Accordion accordion = new Accordion();

        for (Map.Entry<String, HashMap<String, Loi>> evenement : Avion.attentesEvenements.entrySet())
        {
            String nom = evenement.getKey();
            TitledPane titledPane = new TitledPane();
            titledPane.setText(nom);
            VBox vBoxEvenement = new VBox();
            HashMap<String, Loi> attentes = evenement.getValue();
            for (Map.Entry<String, Loi> attente : attentes.entrySet())
            {
                String nomAttente = attente.getKey();
                Label labelAttente = new Label(nomAttente);
                vBoxEvenement.getChildren().add(labelAttente);
                Loi loi = attente.getValue();
                Label labelLoi = new Label("Loi" + " " + ":" + " " + loi.getNom());
                vBoxEvenement.getChildren().add(labelLoi);
                Label labelEsperance = new Label();
                Object esperance = loi.getEsperance();
                if (loi.getEsperance() != null) labelEsperance.setText(String.format("%.2f min", esperance));
                else labelEsperance.setText("Il n'y rien à espérer");
                for (Parametre parametre : loi.getParametres())
                {
                    HBox hBoxParametre = new HBox();
                    Label labelParametre = new Label(parametre.getNom());
                    hBoxParametre.getChildren().add(labelParametre);
                    Slider slider = new Slider();
                    double min = parametre.getMin();
                    double max = parametre.getMax();
                    slider.setMin(min);
                    slider.setMax(max);
                    slider.setShowTickLabels(true);
                    slider.setShowTickMarks(true);
                    slider.setMajorTickUnit((max - min)/2);
                    slider.setValue(parametre.getVal());
                    Label labelVal = new Label();
                    labelVal.setText(String.format("%.2f", parametre.getVal()));
                    slider.valueProperty().addListener((ov, ancienneVal, nouvelleVal) -> {
                        parametre.setVal((Double) nouvelleVal);
                        labelVal.setText(String.format("%.2f", parametre.getVal()));
                        Object espe = loi.getEsperance();
                        if (loi.getEsperance() != null) labelEsperance.setText(String.format("%.2f min", espe));
                        else labelEsperance.setText("Il n'y rien à espérer");
                    });
                    hBoxParametre.getChildren().add(slider);
                    hBoxParametre.getChildren().add(labelVal);
                    vBoxEvenement.getChildren().add(hBoxParametre);
                }
                vBoxEvenement.getChildren().add(labelEsperance);
            }
            titledPane.setContent(vBoxEvenement);
            titledPane.setExpanded(true);
            accordion.getPanes().add(titledPane);
        }
        vBox.getChildren().add(accordion);

        StackPane stackPane = new StackPane();

        canvas_background = new Canvas(largeur, hauteur);
        gc_bg = canvas_background.getGraphicsContext2D();

        canvas_avion = new Canvas(largeur, hauteur);
        gc_av = canvas_avion.getGraphicsContext2D();

        stackPane.getChildren().add(canvas_background);
        stackPane.getChildren().add(canvas_avion);

        hBox.getChildren().add(vBox);
        hBox.getChildren().add(stackPane);

        Scene s = new Scene(hBox, largeur, hauteur);
        stage.setScene(s);
        stage.setTitle("Aeroport");
        stage.show();

        AeroportGraphique.chargerImage();
        PisteGraphique.chargerImage();
        AvionGraphique.chargerImages();
        TerminalGraphique.chargerImage();
        TaxiwayGraphique.chargerImage();

        initialisation();
    }

    private void initialisation()
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 10:34:47.6789");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47.6789");
        Aeroport aeroport = new Aeroport();
        aeroportGraphique = new AeroportGraphique(aeroport);
        aeroportGraphique.peindre(gc_bg, 0, 0, largeur, hauteur);
        eng = new SimuEngine(start, end);
        new Thread(aeroport.getTourDeControle()).start();

        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));
        eng.addEntity(new Avion(eng));

        AnimationTimer timer = new AnimationTimer() {
            Boolean fini = true;
            Boolean attente = false;
            Boolean maj = false;
            int c = 0;
            @Override
            public void handle(long l) {
                if (attente) {
                    c ++;
                    if (c >= 10) {
                        attente = false;
                        c = 0;
                    }
                }
                else if (fini) {
                    //Thread t = new Thread(eng);
                    //t.start();
                    labelHeure.setText(eng.getCurrentDate().toString());
                    labelEvenement.setText(eng.getCurrentEvent().toString());
                    eng.simulationStep(); //§!!!!!!!§§§§§TRES PROBLEMATIQUE
                    attente = true;
                    maj = true;
                    fini = false;
                }
                else {
                    fini = graphic(maj);
                    if (maj) maj = false;
                }
            }
        };
        timer.start();
    }

    public Boolean graphic(Boolean maj)
    {
        if (maj) {
            for (SimEntity simEntity : eng.getEntityList()) {
                if (simEntity instanceof Avion) {
                    Avion avion = (Avion) simEntity;
                    Point point = aeroportGraphique.obtenirPoint(avion.getEtat(),
                            avion.getConsigne());
                    AvionGraphique avionGraphique;
                    if (!mapAvions.containsKey(avion)) {
                        avionGraphique = new AvionGraphique();
                        mapAvions.put(avion, avionGraphique);
                    }
                    else {
                        avionGraphique = mapAvions.get(avion);
                    }
                    if (!Objects.isNull(point)) avionGraphique.sauvegarderFuturePosition(point);
                }
            }
        }
        int c = 0;
        gc_av.clearRect(0, 0, largeur, hauteur);
        for (Map.Entry<Avion, AvionGraphique> entree : mapAvions.entrySet()) {
            AvionGraphique avionGraphique = entree.getValue();
            Avion avion = entree.getKey();
            if (avionGraphique.avancerEtPeindreAvion(gc_av, avion.getEtat())) {
                c++;
            }
        }
        return (c == mapAvions.size());
    }

    public static void main(String[] args)
    {
        launch();
    }
}
