/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distributed;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ishashori
 */
public class Server extends Thread {

      Server(){
        start();
    }
    
    public  void run() {
        String clientSentence;
        String capitalizedSentence;


        Socket connectionSocket;
        try {
            System.out.println("IN Server");
            ServerSocket welcomeSocket = new ServerSocket(10055);
            connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence + '\n';
            outToClient.writeBytes(capitalizedSentence);
            welcomeSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
