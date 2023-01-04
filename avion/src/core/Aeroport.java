package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Aeroport {

    public static class Piste implements Serializable {
        Boolean occupee = false;

        private final ArrayList<Terminal> terminaux = new ArrayList<>();
        HashMap<Integer, Terminal> mapTerminaux = new HashMap<>();

        public Terminal ajouterTerminal(){
            assert terminaux.size() < 3;
            Terminal terminal = new Terminal(this);
            terminaux.add(terminal);
            mapTerminaux.put(terminal.hashCode(), terminal);
            return terminal;
        }

        public ArrayList<Terminal> getTerminaux() {
            return terminaux;
        }
    }

    private final ArrayList<Piste> pistes = new ArrayList<>();
    HashMap<Integer, Piste> mapPistes = new HashMap<Integer, Piste>();
    final private TourDeControleParfaite tourDeControle;

    public Aeroport() {
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
        piste.ajouterTerminal();
        Piste piste1 = ajouterPiste();
        Terminal terminal2 = piste1.ajouterTerminal();
        terminal2.ajouterEmplacement();
        terminal2.ajouterEmplacement();
        terminal2.ajouterEmplacement();
        terminal2.ajouterEmplacement();
        ajouterPiste();
    }

    public Piste ajouterPiste(){
        assert pistes.size() < 3;
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
