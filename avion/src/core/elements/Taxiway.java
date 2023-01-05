package core.elements;

import java.io.Serializable;

public class Taxiway  implements Serializable {
    private Boolean occupee = false;

    public void setOccupee(Boolean occupee) {
        this.occupee = occupee;
    }

    public Boolean getOccupee() {
        return occupee;
    }
}
