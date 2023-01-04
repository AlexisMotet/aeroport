package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;

public class LambdaSimEvent extends SimEvent {

    private final Runnable processLambda;
    private final String name;

    public LambdaSimEvent(SimEntity entity, LogicalDateTime dateOccurence, Runnable processLambda,
                          String name) {
        super(entity, dateOccurence);
        this.processLambda = processLambda;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void process() {
        processLambda.run();
    }

}
