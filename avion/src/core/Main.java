package core;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;

public class Main {
    public static void main(String[] args)  //static method
    {
        LogicalDateTime start = new LogicalDateTime("09/12/2016 10:34:47.6789");
        LogicalDateTime end = new LogicalDateTime("11/12/2016 10:34:47.6789");
        SimuEngine simuEngine = new SimuEngine(start, end);

        simuEngine.addEntity(new Avion(simuEngine));
        simuEngine.addEntity(new Avion(simuEngine));
        simuEngine.simulationLoop();


    }
}
