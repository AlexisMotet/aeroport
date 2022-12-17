package aeroport;

import core.Avion;
import core.TourDeControleParfaite;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimuEngine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Timeline;

public class App extends Application {

    static SimuEngine eng;
    static Piste pisteTW1, pisteTW2;
    static GraphicsContext gc;

    @Override
    public void start(Stage stage) throws IOException {
        StackPane pane = new StackPane();

        final Canvas canvas_background = new Canvas(300,300);
        gc = canvas_background.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.fillRect(0,0,300,300);

        pisteTW1 = new Piste(0, 100, 300);
        pisteTW1.peindrePiste(gc, Color.GREY);

        pisteTW2 = new Piste(200, 100, 300);
        pisteTW2.peindrePiste(gc, Color.GREY);

        pane.getChildren().add(canvas_background);

        Scene s = new Scene(pane, 300, 300);
        stage.setScene(s);
        stage.setTitle("Aeroport");
        stage.show();

        initialisation();
    }

    private static void initialisation()
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 10:34:47.6789");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47.6789");
        App.eng = new SimuEngine(start, end);
        TourDeControleParfaite tdc = new TourDeControleParfaite();
        Avion avion = new Avion(eng, tdc);
        eng.addEntity(avion);
        Timer timer = new Timer();
        TimerTask tache = new TimerTask()
        {
            public void run()
            {
                eng.simulationStep(App::graphic);
            }
        };
        timer.scheduleAtFixedRate(tache, 0,1000);
    }

    public static void graphic()
    {
        SimEntity entity;

        while (Objects.nonNull(entity = eng.getNextEntity()))
        {

            if (entity instanceof Avion)
            {
                Avion avion = (Avion) entity;
                switch (avion.piste)
                {
                    case TW1 :
                        pisteTW1.peindreAvion(gc, Color.GREY, avion.position);
                        break;
                    case TW2 :
                        pisteTW2.peindreAvion(gc, Color.GREY, avion.position);
                        break;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        launch();
    }
}
