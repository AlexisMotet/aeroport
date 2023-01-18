package canvas;

import core.outils.OutilDate;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import javafx.scene.paint.Color;

public class CanvasNuit extends ResizableCanvas {
    private double opacite = 0;

    public void changement(LogicalDateTime date)
    {
        if (!OutilDate.checkSiJour(date)) opacite = 0.3;
        else opacite = 0;
        peindre();
    }

    public void peindre() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        getGraphicsContext2D().setGlobalAlpha(opacite);
        getGraphicsContext2D().setFill(Color.DARKBLUE);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    public void resize(double largeur, double hauteur) {
        setWidth(largeur);
        setHeight(hauteur);
        peindre();
    }
}
