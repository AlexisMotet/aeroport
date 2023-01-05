package core.protocole;

public class MessageVoieArrivee extends Message {
    private final Consigne consigne;
    public MessageVoieArrivee(Consigne consigne) {
        this.consigne = consigne;
    }
    public Consigne getConsigne() {
        return consigne;
    }
}
