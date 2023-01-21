package core;

import core.outils.OutilDate;
import enstabretagne.base.logger.Logger;
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
        System.out.println(getCurrentEvent().getDateOccurence());
        if (currentEvent.getEntity() instanceof Avion)
        {
            Avion avion = (Avion) currentEvent.getEntity();
            long retardAtterrissage = avion.getRetardAtterrissage();
            if (retardAtterrissage != -1){
                Logger.Information(currentEvent.getEntity(), "", String.format("retard atterissage; %d",
                        retardAtterrissage/60));
                retardAtterrissageCumule += retardAtterrissage;
                cAtterrissage ++;
                retardMoyenAtterrissage = retardAtterrissageCumule/cAtterrissage;
            }
            long retardDecollage = avion.getRetardDecollage();
            if (retardDecollage != -1){
                Logger.Information(currentEvent.getEntity(), "", String.format("retard decollage; %d",
                        retardDecollage/60));
                retardDecollageCumule += retardDecollage;
                cDecollage ++;
                retardMoyenDecollage = retardDecollageCumule/cDecollage;
            }
            long dureePhaseAtterissage = avion.getDureePhaseAtterissage();
            if (dureePhaseAtterissage != -1)
            {
                if (OutilDate.checkSiWeekEnd(currentDate))
                {
                    Logger.Information(currentEvent.getEntity(), "",
                            String.format("phase atterissage; week-end; %d", dureePhaseAtterissage));
                }
            else {
                    Logger.Information(currentEvent.getEntity(), "",
                            String.format("phase atterissage; semaine; %d", dureePhaseAtterissage));
                }
            }
            long dureePhaseDecollage = avion.getDureePhaseDecollage();
            if (dureePhaseDecollage != -1)
            {
                if (OutilDate.checkSiWeekEnd(currentDate))
                {
                    Logger.Information(currentEvent.getEntity(), "",
                            String.format("phase decollage; week-end; %d", dureePhaseDecollage));
                }
                else {
                    Logger.Information(currentEvent.getEntity(), "",
                            String.format("phase decollage; semaine; %d", dureePhaseDecollage));
                }
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
