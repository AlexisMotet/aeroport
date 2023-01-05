package core;

import core.elements.Aeroport;
import core.elements.Emplacement;
import core.elements.Piste;
import core.elements.Terminal;
import core.protocole.*;
import core.radio.RadioClient;
import core.radio.RadioServeur;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class TourDeControleParfaite implements Runnable {
    private final Aeroport aeroport;
    private final HashMap<Integer, Consigne> avionsConnus = new HashMap<>();
    public TourDeControleParfaite(Aeroport aeroport) {
        this.aeroport = aeroport;
    }
    @Override
    public void run() {
        try {
            RadioServeur radio = new RadioServeur(5430);
            while (true)
            {
                Message message = radio.recevoirMessage();
                eMessage msg = eMessage.valueOf(message.getClass().getSimpleName());
                switch (msg) {
                    case MessageArrivee -> {
                        Message messageVoieArrivee = trouverLaVoieArrivee();
                        radio.envoyerMessage(messageVoieArrivee);
                        if (messageVoieArrivee instanceof MessageVoieArrivee) {
                            MessageVoieArrivee msgVoieArrivee =
                                    (MessageVoieArrivee) messageVoieArrivee;
                            Piste piste = aeroport.mapPistes.get(
                                    msgVoieArrivee.getConsigne().getPiste());
                            piste.setOccupee(false);
                            Terminal terminal = piste.getMapTerminaux().get(
                                    msgVoieArrivee.getConsigne().getTerminal());
                            terminal.getTW1().setOccupee(true);
                            Emplacement emplacement = terminal.getMapEmplacements().get(
                                    msgVoieArrivee.getConsigne().getEmplacement());
                            emplacement.setOccupe(true);
                        }
                    }
                    case MessageFinDeVol -> {
                        MessageFinDeVol messageFinDeVol =
                                (MessageFinDeVol) message;
                        Piste piste = aeroport.mapPistes.get(
                                messageFinDeVol.getConsigne().getPiste());
                        piste.setOccupee(false);
                        Terminal terminal = piste.getMapTerminaux().get(
                                messageFinDeVol.getConsigne().getTerminal());
                        terminal.getTW1().setOccupee(false);
                        radio.envoyerMessage(new MessageOk());
                    }
                    case MessageDepart -> {
                        MessageDepart messageDepart =
                                (MessageDepart) message;
                        Piste piste = aeroport.mapPistes.get(
                                messageDepart.getConsigne().getPiste());
                        Terminal terminal = piste.getMapTerminaux().get(
                                messageDepart.getConsigne().getTerminal());
                        Message messageCheckVoie = checkVoieDepart(terminal);
                        radio.envoyerMessage(messageCheckVoie);
                        if (messageCheckVoie instanceof MessageOk) {
                            piste.setOccupee(true);
                            terminal.getTW2().setOccupee(true);
                        }
                    }
                    case MessageDecollage -> {
                        MessageDecollage messageDecollage =
                                (MessageDecollage) message;
                        Piste piste = aeroport.mapPistes.get(
                                messageDecollage.getConsigne().getPiste());
                        piste.setOccupee(false);
                        Terminal terminal = piste.getMapTerminaux().get(
                                messageDecollage.getConsigne().getTerminal());
                        terminal.getTW2().setOccupee(false);
                        Emplacement emplacement = terminal.getMapEmplacements().get(
                                messageDecollage.getConsigne().getEmplacement());
                        emplacement.setOccupe(false);
                        radio.envoyerMessage(new MessageOk());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Message trouverLaVoieArrivee(){
        if (avionsConnus.size() > 99) return new MessageNok();
        for (Piste piste : aeroport.getPistes()){
            if (piste.getOccupee()) continue;
            for (Terminal terminal : piste.getTerminaux()){
                if (terminal.getTW1().getOccupee()) continue;
                for (Emplacement emplacement : terminal.getEmplacements())
                {
                    if (emplacement.getOccupe()) continue;
                    Consigne consigne = new Consigne(piste.hashCode(),
                            terminal.hashCode(), emplacement.hashCode());
                    avionsConnus.put(avionsConnus.size(), consigne);
                    return new MessageVoieArrivee(consigne);
                }
            }
        }
        return new MessageNok();
    }

    public Message checkVoieDepart(Terminal terminal){
        Piste piste = terminal.getPiste();
        if (terminal.getTW2().getOccupee()) return new MessageNok();
        if (piste.getOccupee()) return new MessageNok();
        return new MessageOk();
    }


    public static void main(String[] args)
    {

        Aeroport aeroport = new Aeroport();
        TourDeControleParfaite tourDeControle = new TourDeControleParfaite(aeroport);
        Thread t = new Thread(tourDeControle);
        t.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            RadioClient radio = new RadioClient(5430);
            radio.envoyerMessage(new MessageArrivee());
            Message msg = radio.recevoirMessage();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            TimeUnit.SECONDS.sleep(40);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
