package core;

import core.elements.Aeroport;
import core.protocole.*;
import core.radio.RadioClient;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TourDeControleTest {
    @Test
    public void test(){
        Aeroport aeroport = new Aeroport("");
        TourDeControleParfaite tourDeControle = new TourDeControleParfaite(aeroport);
        Thread t = new Thread(tourDeControle);
        t.start();
        try {
            RadioClient radio = new RadioClient(5430);
            radio.envoyerMessage(new MessageArrivee());
            Message msg = radio.recevoirMessage();
            if (msg instanceof MessageVoieArrivee) {
                MessageVoieArrivee msgVoieArrivee = (MessageVoieArrivee) msg;
                assertNotNull(msgVoieArrivee.getConsigne());
                radio.envoyerMessage(new MessageFinDeVol(msgVoieArrivee.getConsigne()));
                msg = radio.recevoirMessage();
                assertTrue(msg instanceof MessageOk);
                radio.envoyerMessage(new MessageDepart(msgVoieArrivee.getConsigne()));
                msg = radio.recevoirMessage();
                assertTrue(msg instanceof MessageOk);
                radio.envoyerMessage(new MessageDecollage(msgVoieArrivee.getConsigne()));
                msg = radio.recevoirMessage();
                assertTrue(msg instanceof MessageOk);
            }
            else if (msg instanceof MessageNok)
            {
                System.out.println("probleme");
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
