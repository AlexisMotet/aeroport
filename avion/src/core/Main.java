package core;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;
import enstabretagne.base.math.MoreRandom;

public class Main {
    public static void main(String[] args)  //static method
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 10:34:47.6789");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47.6789");
        SimuEngine simuEngine = new SimuEngine(start, end);
        TourDeControleParfaite tourDeControle = new TourDeControleParfaite();
        simuEngine.addEntity( new Avion(simuEngine, tourDeControle));
        simuEngine.addEntity( new Avion(simuEngine, tourDeControle));
        simuEngine.simulationLoop(null);
        Logger.Terminate();
    }
}
