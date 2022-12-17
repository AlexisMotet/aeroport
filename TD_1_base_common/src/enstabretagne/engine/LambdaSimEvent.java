package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;

public class LambdaSimEvent extends SimEvent {

    private final Runnable processLambda;

    public LambdaSimEvent(SimEntity entity, LogicalDateTime dateOccurence, Runnable processLambda) {
        super(entity, dateOccurence);
        this.processLambda = processLambda;
    }

    @Override
    public void process() {
        processLambda.run();
    }

}
