package core.protocole;

public class MessageFinDeVol extends Message {
    private final Consigne consigne;

    public Consigne getConsigne() {
        return consigne;
    }
    public MessageFinDeVol(Consigne consigne) {
        this.consigne = consigne;
    }
}
