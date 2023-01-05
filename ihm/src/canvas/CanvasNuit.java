package canvas;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import javafx.scene.paint.Color;

public class CanvasNuit extends ResizableCanvas {
    private double opacite = 0;

    final private LogicalDuration nuitSoir = LogicalDuration.ofHours(21);
    final private LogicalDuration nuitMatin = LogicalDuration.ofHours(5);

    public void changement(LogicalDateTime date)
    {
        LogicalDateTime dateCopie = date.getCopy();
        dateCopie = dateCopie.truncateToDays();

        if (date.soustract(dateCopie).compareTo(nuitSoir) > 0 || date.soustract(dateCopie).compareTo(nuitMatin) < 0)
        {
            opacite = 0.6;
        }
        else
        {
            opacite = 0;
        }
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
