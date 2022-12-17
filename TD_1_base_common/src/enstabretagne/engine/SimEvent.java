package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;

public abstract class SimEvent implements Comparable<SimEvent> {
    private LogicalDateTime dateOccurence;
    private SimEntity entity;

    public SimEvent(SimEntity entity, LogicalDateTime dateOccurence) {
        this.entity = entity;
        this.dateOccurence = dateOccurence;
    }

    public LogicalDateTime getDateOccurence() {
        return dateOccurence;
    }

    public void setDateOccurence(LogicalDateTime dateOccurence) {
        this.dateOccurence = dateOccurence;
    }

    public SimEntity getEntity() {
        return entity;
    }

    public void setEntity(SimEntity entity) {
        this.entity = entity;
    }

    public abstract void process();

    @Override
    public int compareTo(SimEvent o) {
        return getDateOccurence().compareTo(o.getDateOccurence());
    }

}