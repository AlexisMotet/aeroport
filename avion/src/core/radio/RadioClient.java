package core.radio;

import core.protocole.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RadioClient implements Radio {
    DatagramSocket r;
    int port;
    Boolean msgEnvoye = false;
    public RadioClient(int port) throws SocketException {
        r = new DatagramSocket();
        this.port = port;
    }
    public void envoyerMessage(Message msg) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        byte[] buffer = baos.toByteArray();
        r.send(new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port));
        msgEnvoye = true;
    }

    public Message recevoirMessage() throws IOException, ClassNotFoundException {
        assert msgEnvoye;
        byte[] buffer = new byte[1024];
        DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
        r.receive(paquet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
        return (Message) ois.readObject();
    }
}
