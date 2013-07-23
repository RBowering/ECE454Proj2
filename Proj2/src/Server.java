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
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author ishashori
 */
public class Server extends Thread {
    private final BlockingQueue q;
      Server(BlockingQueue q){
	this.q = q;
	//start();

    }
    
    public  void run() {
        String clientSentence;
        String capitalizedSentence;


        Socket connectionSocket;
	while(true)
	{
        try {
            System.out.println("IN Server");
	    System.out.println("In Server1. Msg buffer is empty: "+q.isEmpty());
            ServerSocket welcomeSocket = new ServerSocket(10055);
            connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            q.add(clientSentence);
	    System.out.println("In Server2. Msg buffer is empty: "+q.isEmpty());
            System.out.println("Received: " + clientSentence);
            //get peer to run the code
            //capitalizedSentence = clientSentence + '\n';
            //outToClient.writeBytes(capitalizedSentence);
            welcomeSocket.close();
	
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    }
}
