package aeroport;

import aeroport.elementsgraphiques.AeroportGraphique;
import aeroport.elementsgraphiques.PisteGraphique;
import aeroport.elementsgraphiques.TaxiwayGraphique;
import aeroport.elementsgraphiques.TerminalGraphique;
import canvas.CanvasAeroport;
import canvas.CanvasAvion;
import canvas.CanvasNuit;
import canvas.ResizableCanvas;
import core.Avion;
import core.attente.*;
import core.elements.Aeroport;
import core.elements.Piste;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.skin.SpinnerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.*;

public class App extends Application {

    SimuEngine eng;

    CanvasNuit canvasNuit;
    CanvasAvion canvasAvion;
    CanvasAeroport canvasAeroport;
    int hauteur = 600;
    int largeur = 500;

    AeroportGraphique aeroportGraphique;

    HashMap<Avion, AvionGraphique> mapAvions = new HashMap<>();
    VBox vBox;

    HBox hBoxHeure;
    Label labelHeure;

    HBox hBoxEvenement;
    Label labelEvenement;

    @Override
    public void start(Stage stage) {
        HBox hBox = new HBox();

        vBox = new VBox();

        labelHeure = new Label();
        labelHeure.setStyle("-fx-font-size : 24;");
        hBoxHeure = new HBox();
        hBoxHeure.getChildren().add(labelHeure);

        labelEvenement = new Label();
        labelEvenement.setStyle("-fx-font-size : 16;");
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
                ComboBox<eLoi> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableArrayList(eLoi.values()));
                comboBox.setValue(loi.getNom());
                vBoxEvenement.getChildren().add(comboBox);
                VBox vBoxParametre = new VBox();
                Label labelEsperance = new Label();
                EventHandler<ActionEvent> event = e -> {
                    vBoxParametre.getChildren().clear();
                    Loi nouvelleLoi = null;
                    switch (comboBox.getValue()){
                        case TRIANGLE -> nouvelleLoi = new Triangle();
                        case EXPONENTIELLE -> nouvelleLoi = new Exponentielle();
                        case UNIFORME -> nouvelleLoi = new Uniforme();
                    }
                    for (Parametre parametre : nouvelleLoi.getParametres())
                    {
                        vBoxParametre.getChildren().add(creerParametre(parametre, nouvelleLoi, labelEsperance));
                    }
                };
                comboBox.setOnAction(event);
                comboBox.fireEvent(new ActionEvent());
                vBoxEvenement.getChildren().add(vBoxParametre);
                vBoxEvenement.getChildren().add(labelEsperance);
            }
            titledPane.setContent(vBoxEvenement);
            titledPane.setExpanded(true);
            accordion.getPanes().add(titledPane);
        }
        vBox.getChildren().add(accordion);

        StackPane stackPane = new StackPane();



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


        initialisation(stackPane);
    }

    private HBox creerParametre (Parametre parametre, Loi loi, Label labelEsperance)
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
        Label labelVal = new Label();
        slider.valueProperty().addListener((ov, ancienneVal, nouvelleVal) -> {
            parametre.setVal((Double) nouvelleVal);
            labelVal.setText(String.format("%.2f", parametre.getVal()));
            labelEsperance.setText(String.format("Espérance : %d min", loi.getEsperance().longValue()));
        });
        slider.setValue(parametre.getVal());
        hBoxParametre.getChildren().add(slider);
        hBoxParametre.getChildren().add(labelVal);
        return hBoxParametre;
    }

    private void initialisation(StackPane stackPane)
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 20:58:47");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47");
        Aeroport aeroport = new Aeroport();
        aeroportGraphique = new AeroportGraphique(aeroport);

        canvasAeroport = new CanvasAeroport(aeroportGraphique);
        canvasAvion = new CanvasAvion();
        canvasNuit = new CanvasNuit();

        canvasNuit.resize(largeur, hauteur);
        canvasAeroport.resize(largeur, hauteur);
        canvasAvion.resize(largeur, hauteur);

        stackPane.getChildren().add(canvasAeroport);
        stackPane.getChildren().add(canvasAvion);
        stackPane.getChildren().add(canvasNuit);

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
                    canvasNuit.changement(eng.getCurrentDate());
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
                    if (!mapAvions.containsKey(avion)) {
                        AvionGraphique avionGraphique = new AvionGraphique();
                        mapAvions.put(avion, avionGraphique);
                    }
                }
            }
        }
        int c = 0;
        canvasAvion.effacer();
        for (Map.Entry<Avion, AvionGraphique> entree : mapAvions.entrySet()) {
            AvionGraphique avionGraphique = entree.getValue();
            Avion avion = entree.getKey();
            Point point = aeroportGraphique.obtenirPoint(avion.getEtat(),  avion.getConsigne());
            if (avionGraphique.avancerEtPeindreAvion(canvasAvion.getGraphicsContext2D(), point)) {
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
