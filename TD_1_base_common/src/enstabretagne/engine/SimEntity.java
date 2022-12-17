package enstabretagne.engine;

public abstract class SimEntity {
    final private SimuEngine engine;
    public SimEntity(SimuEngine eng){
        engine = eng;
        engine.addEntity(this);
    }

    public abstract void init();

    public SimuEngine getEngine() {
        return engine;
    }
}
