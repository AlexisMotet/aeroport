package aeroport;

import core.Avion;
import core.TourDeControleParfaite;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

import javafx.animation.Timeline;
import javafx.util.Duration;

public class App extends Application {

    static SimuEngine eng;
    static Piste pisteTW1, pisteTW2;
    static Canvas canvas_background;
    static Canvas canvas_avion;
    static GraphicsContext gc_bg;
    static GraphicsContext gc_av;


    static EnumMap<Avion.enumPosition, Integer> mapPosition;
    static HashMap<Avion, AvionGraphique> mapAvion;


    @Override
    public void start(Stage stage) throws IOException {
        StackPane pane = new StackPane();

        canvas_background = new Canvas(300,300);
        gc_bg = canvas_background.getGraphicsContext2D();

        canvas_avion = new Canvas(300,300);
        gc_av = canvas_avion.getGraphicsContext2D();

        pane.getChildren().add(canvas_background);
        pane.getChildren().add(canvas_avion);

        Scene s = new Scene(pane, 300, 300);
        stage.setScene(s);
        stage.setTitle("Aeroport");
        stage.show();

        initialisation();
    }

    private static void initialisation()
    {
        mapAvion = new HashMap<>();
        mapPosition = new  EnumMap<>(Avion.enumPosition.class);
        mapPosition.put(Avion.enumPosition.DEBUT_PISTE, 10);
        mapPosition.put(Avion.enumPosition.MILIEU_PISTE, 100);
        mapPosition.put(Avion.enumPosition.FIN_PISTE, 200);
        peindreAeroport();
        LogicalDateTime start = new LogicalDateTime("09/12/2016 10:34:47.6789");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47.6789");
        App.eng = new SimuEngine(start, end);
        TourDeControleParfaite tdc = new TourDeControleParfaite();
        Avion avion = new Avion(eng, tdc);
        eng.addEntity(avion);
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
                    eng.simulationStep();
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
    public static class AvionGraphique {
        int derniere_position;
        int position;
        Piste piste;
        public AvionGraphique(int position, Piste piste) {
            this.position = position;
            this.piste = piste;
        }
        public void peindreAvion(GraphicsContext gc, Color couleur, int position)
        {
            if (Objects.isNull(piste)) return;
            gc.setFill(couleur);
            gc.fillRect(piste.x0 + (float) piste.largeur / 2 - 10, position, 20, 20);
        }

        public void sauvegarderPosition()
        {
            this.position = derniere_position;
        }
    }

    public static void peindreAeroport()
    {
        gc_bg.setFill(Color.GREEN);
        gc_bg.fillRect(0,0,300,300);

        pisteTW1 = new Piste(0, 100, 300);
        pisteTW1.peindrePiste(gc_bg, Color.GREY);

        pisteTW2 = new Piste(200, 100, 300);
        pisteTW2.peindrePiste(gc_bg, Color.GREY);
    }

    public static Boolean graphic(Boolean maj)
    {
        if (maj) {
            System.out.println("maj");
            for (SimEntity simEntity : eng.getEntityList()) {
                if (simEntity instanceof Avion) {
                    Avion avion = (Avion) simEntity;
                    Piste piste;
                    int position = mapPosition.get(avion.position);
                    switch (avion.piste) {
                        case TW1 -> piste = pisteTW1;
                        case TW2 -> piste = pisteTW2;
                        default -> piste = null;
                    }
                    if (!mapAvion.containsKey(avion)) {
                        mapAvion.put(avion, new AvionGraphique(position, piste));
                    } else {
                        AvionGraphique avionGraphique = mapAvion.get(avion);
                        avionGraphique.piste = piste;
                        avionGraphique.position = position;
                    }
                }
            }
        }

        int c = 0;
        gc_av.clearRect(0, 0, canvas_avion.getWidth(), canvas_avion.getHeight());
        for (Map.Entry<Avion, AvionGraphique> entree : mapAvion.entrySet()) {
            AvionGraphique avionGraphique = entree.getValue();
            if (avionGraphique.derniere_position == avionGraphique.position) {
                c++;
                avionGraphique.sauvegarderPosition();
            }
            if (avionGraphique.derniere_position > avionGraphique.position) avionGraphique.derniere_position--;
            if (avionGraphique.derniere_position < avionGraphique.position) avionGraphique.derniere_position++;
            avionGraphique.peindreAvion(gc_av, Color.WHITE, avionGraphique.derniere_position);
        }
        return (c == mapAvion.size());
    }

    public static void main(String[] args)
    {
        launch();
    }
}
