package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Terminal implements Serializable {
    Aeroport.Piste piste;

    public Terminal(Aeroport.Piste piste) {
        this.piste = piste;
    }

    public static class Gate implements Serializable {
        Boolean occupe = false;
        Terminal terminal;

        public Gate(Terminal terminal) {
            this.terminal = terminal;
        }
    }
    public static class Taxiway implements Serializable {
        Boolean occupee = false;
    }
    private final Taxiway TW1 = new Taxiway();
    private final Taxiway TW2 = new Taxiway();

    public Taxiway getTW1() {
        return TW1;
    }

    public Taxiway getTW2() {
        return TW2;
    }

    ArrayList<Gate> emplacements = new ArrayList<>();
    HashMap<Integer, Gate> mapEmplacements = new HashMap<Integer, Gate>();

    public void ajouterEmplacement(){
        assert emplacements.size() < 3;
        Gate gate = new Gate(this);
        emplacements.add(gate);
        mapEmplacements.put(gate.hashCode(), gate);
    }

    public ArrayList<Gate> getEmplacements() {
        return emplacements;
    }
}
