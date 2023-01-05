package core.protocole;

public class MessageDepart extends Message {
    private final Consigne consigne;

    public Consigne getConsigne() {
        return consigne;
    }

    public MessageDepart(Consigne consigne) {
        this.consigne = consigne;
    }
}
