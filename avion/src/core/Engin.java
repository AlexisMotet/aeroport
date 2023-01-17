package core;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.time.Duration;

public class Engin extends SimuEngine {
    // :)
    // https://stackoverflow.com/questions/15505515/java-long-primitive-type-maximum-limit
    private long minutesRetardAtterrissageCumule = 0;
    private long minutesRetardDecollageCumule = 0;

    private LogicalDuration retardMoyenAtterrissage = null;
    private LogicalDuration retardMoyenDecollage = null;
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
            LogicalDuration retardAtterrissage = avion.getRetardAtterrissageFinal();
            if (retardAtterrissage != null)
            {
                minutesRetardAtterrissageCumule += retardAtterrissage.getTotalOfMinutes();
                cAtterrissage ++;
                Duration duration = Duration.ofMinutes(minutesRetardAtterrissageCumule);
                long minutes = duration.dividedBy(cAtterrissage).toMinutes();
                retardMoyenAtterrissage = LogicalDuration.ofMinutes(minutes);
            }
            LogicalDuration retardDecollage = avion.getRetardDecollageFinal();
            if (retardDecollage != null)
            {
                minutesRetardDecollageCumule += retardDecollage.getTotalOfMinutes();
                cDecollage ++;
                Duration duration = Duration.ofMinutes(minutesRetardDecollageCumule);
                long minutes = duration.dividedBy(cDecollage).toMinutes();
                retardMoyenDecollage = LogicalDuration.ofMinutes(minutes);
            }
        }
        currentDate = currentEvent.getDateOccurence();
        if (currentDate.compareTo(endDate) > 0) return false;
        sortedEventList.remove(currentEvent);
        currentEvent.process();
        System.gc();
        return true;
    }

    public LogicalDuration getRetardMoyenAtterrissage() {
        return retardMoyenAtterrissage;
    }

    public LogicalDuration getRetardMoyenDecollage() {
        return retardMoyenDecollage;
    }
}
