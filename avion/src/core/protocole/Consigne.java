package core.protocole;

import java.io.Serializable;

public class Consigne implements Serializable {
    public int piste;
    public int terminal;
    public int emplacement;

    public int getPiste() {
        return piste;
    }

    public void setPiste(int piste) {
        this.piste = piste;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public int getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(int emplacement) {
        this.emplacement = emplacement;
    }

    public Consigne(int piste, int terminal, int emplacement) {
        this.piste = piste;
        this.terminal = terminal;
        this.emplacement = emplacement;
    }
}
