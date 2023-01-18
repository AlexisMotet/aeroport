package core.protocole;

import enstabretagne.base.time.LogicalDateTime;

public class MessageDepart extends Message {
    private final Consigne consigne;
    public Consigne getConsigne() {
        return consigne;
    }

    public MessageDepart(Consigne consigne, LogicalDateTime dateDecollage) {
        this.consigne = consigne;
    }
}
