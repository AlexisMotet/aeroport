package core.protocole;

import java.io.Serializable;

public class Consigne implements Serializable {
    private final int piste;
    private final int terminal;
    private final int emplacement;
    private final int numero;
    public int getPiste() {
        return piste;
    }

    public int getTerminal() {
        return terminal;
    }

    public int getEmplacement() {
        return emplacement;
    }

    public int getNumero() {
        return numero;
    }


    public Consigne(int piste, int terminal, int emplacement, int numero) {
        this.piste = piste;
        this.terminal = terminal;
        this.emplacement = emplacement;
        this.numero = numero;
    }


}
