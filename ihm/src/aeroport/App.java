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
import core.Engin;
import core.attente.*;
import core.elements.Aeroport;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class App extends Application {
    Engin eng;

    CanvasNuit canvasNuit;
    CanvasAvion canvasAvion;
    CanvasAeroport canvasAeroport;
    ResizableCanvas canvasAnimation;
    int hauteur = 600;
    int largeur = 1000;

    AeroportGraphique aeroportGraphique;

    HashMap<Avion, AvionGraphique> mapAvions = new HashMap<>();

    Label labelHeure;
    Label labelEvenement;
    Label labelRetardAtterrissage;
    Label labelRetardDecollage;

    @Override
    public void start(Stage stage) {
        HBox hBox = new HBox();

        HBox littleHBox = new HBox();

        VBox vBox = new VBox();

        labelHeure = new Label(
                (new LogicalDateTime("01/01/2000 00:00:00")).toString());
        labelHeure.setStyle("-fx-font-size : 30;");

        labelEvenement = new Label();
        labelEvenement.setStyle("-fx-font-size : 14;");

        labelRetardAtterrissage = new Label();
        labelRetardAtterrissage.setStyle("-fx-font-size : 14;");

        labelRetardDecollage = new Label();
        labelRetardDecollage.setStyle("-fx-font-size : 14;");

        vBox.getChildren().add(labelHeure);
        vBox.getChildren().add(new Label("Evenement :"));
        vBox.getChildren().add(labelEvenement);
        vBox.getChildren().add(new Label("Retard moyen a l'atterrissage :"));
        vBox.getChildren().add(labelRetardAtterrissage);
        vBox.getChildren().add(new Label("Retard moyen au decollage :"));
        vBox.getChildren().add(labelRetardDecollage);
        vBox.setSpacing(10);

        Accordion accordion = new Accordion();

        for (Map.Entry<String, HashMap<String, Loi>> evenement : Avion.attentesEvenements.entrySet())
        {
            String nom = evenement.getKey();
            TitledPane titledPane = new TitledPane();
            titledPane.setText(nom);
            Accordion littleAccordion = new Accordion();
            littleAccordion.setPadding(new Insets(0, 0, 0, 10));
            HashMap<String, Loi> attentes = evenement.getValue();
            for (Map.Entry<String, Loi> attente : attentes.entrySet())
            {
                String nomAttente = attente.getKey();
                TitledPane littleTitledPane = new TitledPane();
                littleTitledPane.setText(nomAttente);
                Loi loi = attente.getValue();
                ComboBox<eLoi> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableArrayList(eLoi.values()));
                comboBox.setValue(loi.getNom());
                VBox vBoxAttente = new VBox();
                vBoxAttente.setSpacing(10);
                vBoxAttente.getChildren().add(comboBox);
                VBox vBoxParametre = new VBox();
                Label labelEsperance = new Label();
                EventHandler<ActionEvent> event = e -> {
                    vBoxParametre.getChildren().clear();
                    Loi nouvelleLoi = null;
                    switch (comboBox.getValue()){
                        case LOI_TRIANGULAIRE -> nouvelleLoi = new Triangulaire();
                        case LOI_EXPONENTIELLE -> nouvelleLoi = new Exponentielle();
                        case LOI_UNIFORME -> nouvelleLoi = new Uniforme();
                        case LOI_GAUSSIENNE -> nouvelleLoi = new Gaussienne();
                    }
                    for (Parametre parametre : nouvelleLoi.getParametres())
                    {
                        vBoxParametre.getChildren().add(creerParametre(parametre, nouvelleLoi, labelEsperance));
                    }
                    attentes.replace(nomAttente, nouvelleLoi);
                };
                comboBox.setOnAction(event);
                comboBox.fireEvent(new ActionEvent());
                vBoxAttente.getChildren().add(vBoxParametre);
                vBoxAttente.getChildren().add(labelEsperance);
                littleTitledPane.setContent(vBoxAttente);
                littleTitledPane.setExpanded(true);
                littleAccordion.getPanes().add(littleTitledPane);
            }
            titledPane.setContent(littleAccordion);
            titledPane.setExpanded(true);
            accordion.getPanes().add(titledPane);
        }
        littleHBox.getChildren().add(vBox);
        littleHBox.getChildren().add(accordion);

        littleHBox.setPadding(new Insets(10, 10, 10, 10));
        littleHBox.setSpacing(10);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(littleHBox);
        scrollPane.setStyle("-fx-focus-color: transparent;" +
                            "-fx-faint-focus-color: transparent;" +
                            "-fx-border-color : grey;");

        hBox.getChildren().add(scrollPane);
        StackPane stackPane = new StackPane();

        hBox.getChildren().add(stackPane);

        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);

        Scene s = new Scene(hBox, largeur, hauteur);
        s.getStylesheets().add("style.css");
        stage.setScene(s);
        stage.setTitle("Aeroport");

        hBox.applyCss();
        hBox.layout();
        scrollPane.setMinWidth(scrollPane.getWidth());

        AeroportGraphique.chargerImage();
        PisteGraphique.chargerImage();
        AvionGraphique.chargerImages();
        TerminalGraphique.chargerImage();
        TaxiwayGraphique.chargerImage();

        initialisation(stackPane);
        stage.show();
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
        LogicalDateTime start = new LogicalDateTime("09/12/2016 04:55:47");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47");

        Aeroport aeroport = new Aeroport();
        aeroportGraphique = new AeroportGraphique(aeroport);

        canvasAeroport = new CanvasAeroport(aeroportGraphique);
        canvasAvion = new CanvasAvion();
        canvasNuit = new CanvasNuit();
        canvasAnimation = new ResizableCanvas();

        Rectangle2D rect = Screen.getPrimary().getBounds();

        canvasAnimation.resize(rect.getWidth(), rect.getHeight());
        canvasNuit.resize(rect.getWidth(), rect.getHeight());
        canvasAeroport.resize(rect.getWidth(), rect.getHeight());
        canvasAvion.resize(rect.getWidth(), rect.getHeight());

        stackPane.getChildren().add(canvasAeroport);
        stackPane.getChildren().add(canvasAvion);
        stackPane.getChildren().add(canvasAnimation);
        stackPane.getChildren().add(canvasNuit);

        eng = new Engin(start, end);
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

        AnimationTimer timer = new AnimationTimer() {
            Boolean fini = true;
            Boolean attente = false;
            Boolean maj = false;
            int c = 0;
            @Override
            public void handle(long l) {
                canvasAnimation.clear();
                aeroportGraphique.animationTourDeControle(canvasAnimation.getGraphicsContext2D());
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

                    LogicalDuration retardMoyenAtterrissage = eng.getRetardMoyenAtterrissage();
                    if (retardMoyenAtterrissage == null) labelRetardAtterrissage.setText("non defini");
                    else labelRetardAtterrissage.setText(retardMoyenAtterrissage.toString());

                    LogicalDuration retardMoyenDecollage = eng.getRetardMoyenDecollage();
                    if (retardMoyenDecollage == null) labelRetardDecollage.setText("non defini");
                    else labelRetardDecollage.setText(retardMoyenDecollage.toString());

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
                        AvionGraphique avionGraphique = new AvionGraphique(
                                aeroportGraphique.genererPointDuCiel());
                        mapAvions.put(avion, avionGraphique);
                    }
                }
            }
        }
        int c = 0;
        canvasAvion.clear();
        for (Map.Entry<Avion, AvionGraphique> entree : mapAvions.entrySet()) {
            AvionGraphique avionGraphique = entree.getValue();
            Avion avion = entree.getKey();
            Point point = aeroportGraphique.obtenirPoint(avion.getEtat(), avion.getConsigne());
            if (avionGraphique.avancerEtPeindreAvion(canvasAvion.getGraphicsContext2D(),
                    point, avion.getEtat(), avion.getConsigne())) {
                c++;
            }
        }
        return c == mapAvions.size();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
