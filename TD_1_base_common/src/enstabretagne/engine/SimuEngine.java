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

public class SimuEngine {
    final private SortedList<SimEvent> sortedEventList;
    final private List<SimEntity> entityList;
    private LogicalDateTime currentDate;
    final private LogicalDateTime endDate;


    public SimuEngine(LogicalDateTime currentDate, LogicalDateTime endDate) {
        sortedEventList = new SortedList<>();
        this.currentDate = currentDate;
        this.endDate = endDate;
        entityList = new ArrayList<>();
    }

    public void postEvent(SimEvent simEvent) {
        sortedEventList.add(simEvent);
    }

    public void postEvent(SimEntity entity, LogicalDateTime date, Runnable action) {
        postEvent(new LambdaSimEvent(entity, date, action));
    }

    public LogicalDateTime getCurrentDate() {
        return currentDate;
    }

    public boolean simulationStep(Runnable graphic) {
        if (sortedEventList.size() == 0) return false;
        SimEvent currentEvent = sortedEventList.first();
        currentDate = currentEvent.getDateOccurence();
        if (currentDate.compareTo(endDate) > 0) return false;
        sortedEventList.remove(currentEvent);
        currentEvent.process();
        //graphic.run();
        System.gc();
        return true;
    }

    public void simulationLoop(Runnable graphic) {
        while (simulationStep(graphic));
    }

    public void addEntity(SimEntity entity) {
        entityList.add(entity);
    }

    public SimEntity getNextEntity()
    {
        ListIterator<SimEntity> iterator = entityList.listIterator();
        if (iterator.hasNext()) return iterator.next();
        return null;
    }

    public List<SimEntity> request(Predicate<SimEntity> filter) {
        List<SimEntity> result = new ArrayList<>();
        for (SimEntity entity : entityList) {
            if (filter.test(entity)) result.add(entity);
        }
        return result;
    }

}
