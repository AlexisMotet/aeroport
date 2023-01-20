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
import core.EntiteSupreme;
import core.attente.*;
import core.elements.Aeroport;
import core.outils.OutilDate;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private Engin eng;
    private CanvasAeroport canvasAeroport;
    private CanvasAvion canvasAvion;
    private CanvasNuit canvasNuit;
    private ResizableCanvas canvasAnimation;

    private final StackPane stackPane = new StackPane();
    private AeroportGraphique aeroportGraphique;
    private final HashMap<Avion, AvionGraphique> mapAvions = new HashMap<>();
    private Label labelHeure;

    private Label labelEntite;
    private Label labelEvenement;

    private Label labelNombreAvions;
    private Label labelAffluence;

    private Label labelFrequence;
    private Label labelRetardAtterrissage;
    private Label labelRetardDecollage;


    @Override
    public void start(Stage stage) {
        HBox hBox = new HBox();

        HBox littleHBox = new HBox();

        VBox vBox = new VBox();

        labelHeure = new Label(
                (new LogicalDateTime("01/01/2000 00:00:00")).toString());
        labelHeure.setStyle("-fx-font-size : 30;");

        HBox hBoxVitesse = new HBox();
        Label labelVitesse = new Label();
        labelVitesse.setText(String.valueOf(AvionGraphique.vitesse));
        labelVitesse.setStyle("-fx-font-size : 14;");
        Button boutonMoins = new Button("-");
        boutonMoins.setStyle("-fx-font-size : 14;");
        boutonMoins.setOnAction(e-> {
            AvionGraphique.vitesse --;
            labelVitesse.setText(String.valueOf(AvionGraphique.vitesse));
        });
        Button boutonPlus = new Button("+");
        boutonPlus.setOnAction(e-> {
            AvionGraphique.vitesse ++;
            labelVitesse.setText(String.valueOf(AvionGraphique.vitesse));
        });
        boutonPlus.setStyle("-fx-font-size : 14;");
        hBoxVitesse.setSpacing(10);
        hBoxVitesse.setAlignment(Pos.BASELINE_LEFT);

        labelEntite = new Label();
        labelEntite.setStyle("-fx-font-size : 14;");

        labelEvenement = new Label();
        labelEvenement.setStyle("-fx-font-size : 14;");

        labelNombreAvions = new Label();
        labelNombreAvions.setStyle("-fx-font-size : 14;");

        labelAffluence = new Label();
        labelAffluence.setStyle("-fx-font-size : 14;");

        labelFrequence = new Label();
        labelFrequence.setStyle("-fx-font-size : 14;");

        labelRetardAtterrissage = new Label();
        labelRetardAtterrissage.setStyle("-fx-font-size : 14;");

        labelRetardDecollage = new Label();
        labelRetardDecollage.setStyle("-fx-font-size : 14;");

        vBox.getChildren().add(labelHeure);
        vBox.getChildren().add(new Label("Vitesse :"));
        hBoxVitesse.getChildren().add(labelVitesse);
        hBoxVitesse.getChildren().add(boutonMoins);
        hBoxVitesse.getChildren().add(boutonPlus);
        vBox.getChildren().add(hBoxVitesse);
        vBox.getChildren().add(new Label("Entite courante :"));
        vBox.getChildren().add(labelEntite);
        vBox.getChildren().add(new Label("Evenement courant :"));
        vBox.getChildren().add(labelEvenement);
        vBox.getChildren().add(new Label("Nombre d'avions :"));
        vBox.getChildren().add(labelNombreAvions);
        vBox.getChildren().add(new Label("Affluence :"));
        vBox.getChildren().add(labelAffluence);
        vBox.getChildren().add(new Label("Frequence :"));
        vBox.getChildren().add(labelFrequence);
        vBox.getChildren().add(new Label("Retard moyen a l'atterrissage :"));
        vBox.getChildren().add(labelRetardAtterrissage);
        vBox.getChildren().add(new Label("Retard moyen au decollage :"));
        vBox.getChildren().add(labelRetardDecollage);
        vBox.setSpacing(10);

        Accordion accordion = new Accordion();

        for (Map.Entry<String, HashMap<String, Loi>> evenement : Avion.attentesEvenements.entrySet())
        {
            String nom = evenement.getKey();
            HashMap<String, Loi> attentes = evenement.getValue();
            TitledPane titledPane = new TitledPane();
            titledPane.setText(nom);
            Accordion littleAccordion = new Accordion();
            littleAccordion.setPadding(new Insets(0, 0, 0, 10));
            for (Map.Entry<String, Loi> attente : attentes.entrySet())
            {
                String nomAttente = attente.getKey();
                Loi loi = attente.getValue();
                TitledPane littleTitledPane = new TitledPane();
                littleTitledPane.setText(nomAttente);
                ObservableList<String> options =
                        FXCollections.observableArrayList(Loi.nomsLois);
                ComboBox<String> comboBox = new ComboBox<>(options);
                comboBox.setValue(loi.getNom());
                VBox vBoxAttente = new VBox();
                vBoxAttente.setSpacing(10);
                vBoxAttente.getChildren().add(comboBox);
                VBox vBoxParametre = new VBox();
                Label labelEsperance = new Label();
                Label labelEcartType = new Label();
                EventHandler<ActionEvent> event = e -> {
                    vBoxParametre.getChildren().clear();
                    Loi nouvelleLoi;
                    switch (comboBox.getValue()){
                        case "Loi triangulaire" -> nouvelleLoi = new Triangulaire();
                        case "Loi exponentielle" -> nouvelleLoi = new Exponentielle();
                        case "loi gaussienne" -> nouvelleLoi = new Gaussienne();
                        default -> nouvelleLoi = new Uniforme();
                    }
                    for (Parametre parametre : nouvelleLoi.getParametres())
                    {
                        vBoxParametre.getChildren().add(creerParametre(parametre,
                                nouvelleLoi, labelEsperance, labelEcartType));
                    }
                    attentes.replace(nomAttente, nouvelleLoi);
                };
                comboBox.setOnAction(event);
                comboBox.setValue(loi.getNom());
                for (Parametre parametre : loi.getParametres())
                {
                    vBoxParametre.getChildren().add(creerParametre(parametre,
                            loi, labelEsperance, labelEcartType));
                }
                vBoxAttente.getChildren().add(vBoxParametre);
                vBoxAttente.getChildren().add(labelEsperance);
                vBoxAttente.getChildren().add(labelEcartType);
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
        hBox.getChildren().add(stackPane);

        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);

        Scene s = new Scene(hBox, 1000, 600);
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
        TaxiwayGraphique.chargerImage();initialisationAeroport(stackPane);
        stage.show();
    }

    private HBox creerParametre (Parametre parametre, Loi loi,
                                 Label labelEsperance, Label labelEcartType)
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
            labelEsperance.setText(String.format("Esperance : %d min", loi.getEsperance().longValue()));
            labelEcartType.setText(String.format("Ecart-type : %d min", loi.getEcartType().longValue()));
        });
        slider.setValue(parametre.getVal());
        hBoxParametre.getChildren().add(slider);
        hBoxParametre.getChildren().add(labelVal);
        return hBoxParametre;
    }

    private void initialisationAeroport(StackPane stackPane)
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 20:55:47");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47");

        Aeroport aeroport = new Aeroport("C:\\Users\\alexi\\PROJETS\\SIMULATION_DE_SYSTEMES\\aeroport\\avion\\config.txt");
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
        EntiteSupreme entiteSupreme = new EntiteSupreme(eng);
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
                    eng.simulationStep();
                    LogicalDateTime date = eng.getCurrentDate();
                    canvasNuit.changement(date);
                    labelHeure.setText(date.toString());
                    labelEntite.setText(eng.getCurrentEvent().getEntity().toString());
                    labelEvenement.setText(eng.getCurrentEvent().toString());
                    switch (OutilDate.obtenirAffluence(date)){
                        case NORMALE -> labelAffluence.setText("Affluence normale");
                        case WEEK_END -> labelAffluence.setText("Week-end");
                        case HEURE_DE_POINTE-> labelAffluence.setText("Heure de pointe");
                    }
                    labelFrequence.setText(String.format("%dmin", entiteSupreme.getFrequence()));
                    long retard = eng.getRetardMoyenAtterrissage();
                    int minutes = (int) retard / 60;
                    int secondes = (int) retard % 60;
                    labelRetardAtterrissage.setText(String.format("%dmin%02ds", minutes, secondes));
                    retard = eng.getRetardMoyenDecollage();
                    minutes = (int) retard / 60 ;
                    secondes = (int) retard % 60;
                    labelRetardDecollage.setText(String.format("%dmin%02ds", minutes, secondes));

                    attente = true;
                    maj = true;
                    fini = false;
                }
                else {
                    fini = majGraphique(maj);
                    if (maj) maj = false;
                }
            }
        };
        timer.start();
    }

    public Boolean majGraphique(Boolean maj)
    {
        if (maj) {
            int i = 0;
            for (SimEntity simEntity : eng.getEntityList()) {
                if (simEntity instanceof Avion) {
                    i ++;
                    Avion avion = (Avion) simEntity;
                    if (!mapAvions.containsKey(avion)) {
                        AvionGraphique avionGraphique = new AvionGraphique(
                                aeroportGraphique.genererPointDuCiel(),
                                aeroportGraphique.genererIdPointDuCiel());
                        mapAvions.put(avion, avionGraphique);
                    }
                }
            }
            labelNombreAvions.setText(String.valueOf(i));
        }
        int c = 0;
        canvasAvion.clear();
        ArrayList<Avion> aSupprimer = new ArrayList<>();
        for (Map.Entry<Avion, AvionGraphique> entree : mapAvions.entrySet()) {
            AvionGraphique avionGraphique = entree.getValue();
            Avion avion = entree.getKey();
            if (!eng.getEntityList().contains(avion)) aSupprimer.add(avion);
            Point point = aeroportGraphique.obtenirPoint(avion.getEtat(), avion.getConsigne());
            if (avionGraphique.avancerEtPeindreAvion(
                    canvasAvion.getGraphicsContext2D(),
                    aeroportGraphique.getMapPointsDuCiel(),
                    point,
                    avion.getEtat(),
                    avion.getConsigne())) {
                c++;
            }
        }
        for (Avion avion : aSupprimer) mapAvions.remove(avion);
        return c == mapAvions.size();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
