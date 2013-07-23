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

public class Test extends Thread
{
	
    public static void main(String[] args) throws Exception 
	{
		BlockingQueue q = new LinkedBlockingQueue<String>();
		Server server = new Server(q);
		PeerRPC prpc = new PeerRPC(q);
		Peer p = new Peer();
		
		new Thread(server).start();
		new Thread(p).start();
		new Thread(prpc).start();

    }
	
	
}
