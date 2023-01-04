package aeroport;

import core.Avion;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
public class InfoAvion extends HBox {

    private Label labelPosition;
    private Label labelZone = new Label();
    private Label labelPiste = new Label();
/*
    public InfoAvion(int position, Avion.enumPosition zone, Avion.enumPiste piste) {
        super();
        this.getChildren().add(labelPosition  = new Label(Integer.toString(position)));
        this.getChildren().add(labelZone  = new Label(zone.toString()));
        this.getChildren().add(labelPiste  = new Label(piste.toString()));
    }

    public void majInfo(int position, Avion.enumPosition zone, Avion.enumPiste piste)
    {
        labelPosition.setText(Integer.toString(position));
        labelZone.setText(zone.toString());
        labelPiste.setText(piste.toString());
    }*/
}
