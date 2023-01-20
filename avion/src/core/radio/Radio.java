package core.radio;

import core.protocole.Message;

import java.io.IOException;

public interface Radio {
    void envoyerMessage(Message msg) throws IOException;
    Message recevoirMessage() throws IOException, ClassNotFoundException ;

}
