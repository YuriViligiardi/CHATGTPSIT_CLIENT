package it.client;

import java.io.IOException;
import java.net.Socket;

public class Controller {
    private Sender sender;
    private Receive receive;

    public Controller(Socket s) throws IOException {
        this.sender = new Sender(s);
        this.receive = new Receive(s, this);
    }

    public void start() {
        sender.start();
        receive.start();
    }

    public void alertUsernameError() throws IOException, InterruptedException {
        System.out.println("Inserire un'altro nome utente:");
        sender.getUsername();
    }
}