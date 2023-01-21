package core.evenements;

import core.Avion;
import core.attentes.Loi;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;

import java.util.HashMap;

public abstract class EvenementAvion extends MonEvenement {
    protected Avion avion;
    public EvenementAvion(SimEntity entite, LogicalDateTime dateOccurence) {
        super(entite, dateOccurence);
        avion = (Avion) entite;
    }

    public static HashMap<String, Loi> getAttentes()
    {
        throw new IllegalStateException("La methode getAttentes doit etre surchargee");
    }

    public static String getNom()
    {
        throw new IllegalStateException("La methode getNom doit etre surchargee");
    }
}
