package core;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimEntity;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

public class Engin extends SimuEngine {
    // :)
    // https://stackoverflow.com/questions/15505515/java-long-primitive-type-maximum-limit
    private long retardAtterrissageCumule = 0;
    private long retardDecollageCumule = 0;
    private long retardMoyenAtterrissage = 0;
    private long retardMoyenDecollage = 0;
    private int cAtterrissage = 0;
    private int cDecollage = 0;
    public Engin(LogicalDateTime currentDate, LogicalDateTime endDate) {
        super(currentDate, endDate);
    }
    public boolean simulationStep() {
        if (sortedEventList.size() == 0) return false;
        SimEvent currentEvent = getCurrentEvent();
        if (currentEvent.getEntity() instanceof Avion)
        {
            Avion avion = (Avion) currentEvent.getEntity();
            long retardAtterrissage = avion.getRetardAtterrissage();
            if (retardAtterrissage != -1){
                retardAtterrissageCumule += retardAtterrissage;
                cAtterrissage ++;
                retardMoyenAtterrissage = retardAtterrissageCumule/cAtterrissage;
            }
            long retardDecollage = avion.getRetardDecollage();
            if (retardDecollage != -1){
                retardDecollageCumule += retardDecollage;
                cDecollage ++;
                retardMoyenDecollage = retardDecollageCumule/cDecollage;
            }
        }
        currentDate = currentEvent.getDateOccurence();
        if (currentDate.compareTo(endDate) > 0) return false;
        sortedEventList.remove(currentEvent);
        currentEvent.process();
        System.gc();
        return true;
    }

    public long getRetardMoyenAtterrissage() {
        return retardMoyenAtterrissage;
    }

    public long getRetardMoyenDecollage() {
        return retardMoyenDecollage;
    }
}
