package core.evenements;

import core.Avion;
import core.EntiteSupreme;
import core.outils.OutilDate;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEntity;

public class CreationAvion extends MonEvenement {
    private final EntiteSupreme entiteSupreme;
    public CreationAvion(SimEntity entity, LogicalDateTime dateOccurence) {
        super(entity, dateOccurence);
        entiteSupreme = (EntiteSupreme) entity;
    }

    @Override
    public void process() {
        LogicalDateTime date = getDateOccurence();
        if (!OutilDate.checkSiJour(date))
        {
            entiteSupreme.getEngine().postEvent(new CreationAvion(entiteSupreme,
                    OutilDate.obtenirProchainMatin(date)));
        }
        else {
            new Avion(entiteSupreme.getEngine());
            LogicalDuration duree = LogicalDuration.ofMinutes(entiteSupreme.getFrequence());
            LogicalDateTime futureDate = LogicalDateTime.add(date, duree);
            entiteSupreme.getEngine().postEvent(new CreationAvion(entiteSupreme, futureDate));
        }
    }

    @Override
    public String toString() {
        return "Creation Avion";
    }
}
