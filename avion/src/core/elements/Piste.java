package core.elements;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Piste implements Serializable {

    private Boolean occupee = false;

    private final ArrayList<Terminal> terminaux = new ArrayList<>();

    private final HashMap<Integer, Terminal> mapTerminaux = new HashMap<>();

    public HashMap<Integer, Terminal> getMapTerminaux() {
        return mapTerminaux;
    }

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

    public Boolean getOccupee() {
        return occupee;
    }
    public void setOccupee(Boolean occupee) {
        this.occupee = occupee;
    }
}
