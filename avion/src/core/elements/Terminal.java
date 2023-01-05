package core.elements;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Terminal implements Serializable {
    private final Piste piste;

    public Piste getPiste() {
        return piste;
    }

    private final Taxiway TW1 = new Taxiway();

    private final Taxiway TW2 = new Taxiway();

    public Terminal(Piste piste) {
        this.piste = piste;
    }

    private final ArrayList<Emplacement> emplacements = new ArrayList<>();

    public HashMap<Integer, Emplacement> getMapEmplacements() {
        return mapEmplacements;
    }

    private final HashMap<Integer, Emplacement> mapEmplacements = new HashMap<>();

    public void ajouterEmplacement(){
        assert emplacements.size() < 3;
        Emplacement gate = new Emplacement(this);
        emplacements.add(gate);
        mapEmplacements.put(gate.hashCode(), gate);
    }

    public ArrayList<Emplacement> getEmplacements() {
        return emplacements;
    }

    public Taxiway getTW1() {
        return TW1;
    }

    public Taxiway getTW2() {
        return TW2;
    }
}
