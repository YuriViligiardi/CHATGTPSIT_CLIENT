package it.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        int port = 3000;
        Socket s = new Socket("localhost", port);
        System.out.println("Socket connesso al server");

        Controller c = new Controller(s);
        c.start();
    }
}