package core.protocole;

public class MessageDecollage extends Message {
    private final Consigne consigne;

    public Consigne getConsigne() {
        return consigne;
    }

    public MessageDecollage(Consigne consigne) {
        this.consigne = consigne;
    }
}
