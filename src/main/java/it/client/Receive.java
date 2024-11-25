package it.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Receive extends Thread {
    // attributes
    private BufferedReader in;
    private Controller c;
    private Socket socket;

    // methods and constructions
    public Receive(Socket s, Controller controller) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.c = controller;
        this.socket = s;
    }

    // funzione che controlla le risposte del server, guardando anche se si trattano
    // o no di errori
    public void check(String[] a) throws IOException, InterruptedException {
        switch (a[0]) {
            case "KO":

                switch (a[1]) {
                    case "user-not-available":
                        System.out.println("Errore, username gia in utilizzo");
                        c.alertUsernameError();
                        break;

                    case "user-not-found":
                        System.out.println("Errore, utente non trovato");
                        break;

                    default:
                        System.out.println("Errore");
                        break;
                }
                break;

            case "JOIN":
                if (a.length > 1) {
                    System.out.println("");
                    System.out.println("Client <" + a[1] + "> si è collegato");
                } else {
                    System.out.println("Client connesso al server");
                }
                break;

            case "ACCEPT":
                if (a.length > 1) {
                    System.out.println("Nuovo nome utente <" + a[1] + "> accettato");
                }
                break;

            case "USERS":
                System.out.println("Lista utenti attivi:");
                System.out.println("(tu)");
                for (int i = 1; i < a.length; i++) {
                    System.out.println(a[i]);
                }
                break;

            case "PRIVATE":
                System.out.println(" ");
                System.out.println("Messaggio privato da " + a[1] + ": ");
                String mesPriv = "";
                for (int i = 2; i < a.length; i++) {
                    mesPriv += a[i] + " ";
                }
                System.out.println(mesPriv);
                break;

            case "GLOBAL":
                System.out.println(" ");
                System.out.println("Messaggio pubblico da " + a[1] + ": ");
                String mesGlob = "";
                for (int i = 2; i < a.length; i++) {
                    mesGlob += a[i] + " ";
                }
                System.out.println(mesGlob);
                break;

            case "BYE":
                System.out.println("L'utente " + a[1] + " si è disconesso");
                break;
                
            default:
                System.out.println(" ");
                break;
        }
    }

    // funzione di start che è la parte centrale della classe Receive e del
    // funzionamento di essa. Inoltre questa sta sempre in ascolto per prendere in
    // input i messaggi/risposte del server
    @Override
    public void run() {
        try {
            boolean control = true;
            while (control) {
                if (socket.isClosed()) {
                    control = false;
                    break;
                } else {
                    String input = in.readLine();
                    if (input == null) {
                        break; // Gestione della chiusura del socket
                    }
                    String[] answer = input.split(" ");
                    check(answer);
                }
            }
        } catch (SocketException e) {
            // Gestione specifica per il socket chiuso
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); // chiude il socket
                in.close(); // Chiudi la risorsa
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}