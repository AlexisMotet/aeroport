package core.elements;


import java.io.Serializable;

public class Emplacement implements Serializable  {
    private Boolean occupe = false;
    Terminal terminal;

    public Emplacement(Terminal terminal) {
        this.terminal = terminal;
    }

    public void setOccupe(Boolean occupe) {
        this.occupe = occupe;
    }

    public Boolean getOccupe() {
        return occupe;
    }
}
