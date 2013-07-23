/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distributed;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ishashori
 */
public class Client extends Thread {
    String ip;
    String port;
    String msg;
    
    Client(String i, String p, String msg){
        ip = i;
	System.out.println("Client IP: "+ip+":"+p);
        port = p;
        this.msg= msg;
        start();
    }

    public void run() {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket;
        try {
            System.out.println("IN CLIENT");
            clientSocket = new Socket(ip, Integer.parseInt(port));

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //sentence = inFromUser.readLine();
            outToServer.writeBytes(msg + '\n');
            //modifiedSentence = inFromServer.readLine();
            //System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
