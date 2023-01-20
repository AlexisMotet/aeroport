package core;

import core.elements.Aeroport;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;

public class Main {
    public static void main(String[] args)
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 20:55:47");
        LogicalDateTime end = new LogicalDateTime("09/12/2016 21:00:00");
        Aeroport aeroport = new Aeroport(args[0]);
        Thread t = new Thread(aeroport.getTourDeControle());
        t.start();
        Engin engin = new Engin(start, end);
        engin.addEntity(new Avion(engin));
        engin.simulationLoop();
        Logger.Terminate();
        aeroport.getTourDeControle().stopper();
        System.out.println("finished");
    }
}
