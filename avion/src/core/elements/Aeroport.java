package core.elements;

import core.TourDeControleParfaite;

import java.util.ArrayList;
import java.util.HashMap;

public class Aeroport {
    private final ArrayList<Piste> pistes = new ArrayList<>();

    public HashMap<Integer, Piste> getMapPistes() {
        return mapPistes;
    }
    private final HashMap<Integer, Piste> mapPistes = new HashMap<>();
    final private TourDeControleParfaite tourDeControle;

    public Aeroport() {
        try {
            tourDeControle = new TourDeControleParfaite(this);
            Piste piste = ajouterPiste();
            Terminal terminal = piste.ajouterTerminal();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            terminal.ajouterEmplacement();
            Terminal terminal1 = piste.ajouterTerminal();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            terminal1.ajouterEmplacement();
            piste.ajouterTerminal();
            Piste piste1 = ajouterPiste();
            Terminal terminal2 = piste1.ajouterTerminal();
            terminal2.ajouterEmplacement();
            terminal2.ajouterEmplacement();
            terminal2.ajouterEmplacement();
            terminal2.ajouterEmplacement();
            Piste piste2 = ajouterPiste();
            Terminal terminal3 = piste2.ajouterTerminal();
            terminal3.ajouterEmplacement();
            terminal3.ajouterEmplacement();
            terminal3.ajouterEmplacement();
            terminal3.ajouterEmplacement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Piste ajouterPiste() throws Exception {
        if (pistes.size() > 3)
        {
            throw new Exception("Le nombre de pistes est limite a 3");
        }
        Piste piste = new Piste();
        pistes.add(piste);
        mapPistes.put(piste.hashCode(), piste);
        return piste;
    }

    public ArrayList<Piste> getPistes() {
        return pistes;
    }


    public TourDeControleParfaite getTourDeControle() {
        return tourDeControle;
    }
}
