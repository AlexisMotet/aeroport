package core.radio;

import core.protocole.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RadioServeur implements Radio {
    DatagramSocket r;
    private int port = -1;
    public RadioServeur(int port) throws SocketException {
        r = new DatagramSocket(port);
    }
    public void envoyerMessage(Message msg) throws IOException {
        assert port != -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        byte[] buffer = baos.toByteArray();
        r.send(new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port));
        port = -1;
    }

    public Message recevoirMessage() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024];
        DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
        r.receive(paquet);
        port = paquet.getPort();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
        return (Message) ois.readObject();
    }
}
