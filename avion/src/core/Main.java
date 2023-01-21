package core;

import core.elements.Aeroport;
import core.outils.MonFournisseurDeDate;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;
import enstabretagne.simulation.basics.ISimulationDateProvider;

public class Main {
    public static void main(String[] args)
    {
        LogicalDateTime start = new LogicalDateTime("01/01/2016 00:00:00");
        LogicalDateTime end = new LogicalDateTime("01/04/2016 00:00:00");
        Engin engin = new Engin(start, end);
        Logger.setDateProvider(new MonFournisseurDeDate(engin));
        Aeroport aeroport = new Aeroport(args[0]);
        Thread t = new Thread(aeroport.getTourDeControle());
        t.start();
        new EntiteSupreme(engin);
        engin.simulationLoop();
        aeroport.getTourDeControle().stopper();
        Logger.Terminate();
        System.exit(0);
    }
}
