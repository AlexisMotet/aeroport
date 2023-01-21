package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.SortedList;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimuEngine implements Runnable {
    protected final SortedList<SimEvent> sortedEventList;
    final protected List<SimEntity> entityList;
    protected LogicalDateTime currentDate;
    protected final LogicalDateTime endDate;
    public SortedList<SimEvent> getSortedEventList() {
        return sortedEventList;
    }

    public SimuEngine(LogicalDateTime currentDate, LogicalDateTime endDate) {
        sortedEventList = new SortedList<>();
        this.currentDate = currentDate;
        this.endDate = endDate;
        entityList = new ArrayList<>();
    }

    public void postEvent(SimEvent simEvent) {
        sortedEventList.add(simEvent);
    }

    public SimEvent getCurrentEvent() {
        return sortedEventList.first();
    }

    public LogicalDateTime getCurrentDate() {
        return currentDate;
    }

    public boolean simulationStep() {
        if (sortedEventList.size() == 0) return false;
        SimEvent currentEvent = getCurrentEvent();
        currentDate = currentEvent.getDateOccurence();
        if (currentDate.compareTo(endDate) > 0) return false;
        sortedEventList.remove(currentEvent);
        currentEvent.process();
        System.gc();
        return true;
    }

    public void simulationLoop() {
        while (simulationStep());
    }

    public void addEntity(SimEntity entity) {
        entityList.add(entity);
    }

    public List<SimEntity> getEntityList() {
        return entityList;
    }

    public List<SimEntity> request(Predicate<SimEntity> filter) {
        List<SimEntity> result = new ArrayList<>();
        for (SimEntity entity : entityList) {
            if (filter.test(entity)) result.add(entity);
        }
        return result;
    }

    @Override
    public void run() {
        simulationStep();
    }
}
