package core.outils;

import core.Engin;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.ISimulationDateProvider;

public class MonFournisseurDeDate implements ISimulationDateProvider {
    Engin eng;

    public MonFournisseurDeDate(Engin eng) {
        this.eng = eng;
    }

    @Override
    public LogicalDateTime SimulationDate() {
        return eng.getCurrentDate();
    }
}
