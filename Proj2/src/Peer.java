import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;



public class Peer {
	
	static List<FileDesc> li = new ArrayList<FileDesc>();
	
	public static void server () throws Exception
	{
		String clientSentence;          
		String capitalizedSentence;          
		ServerSocket welcomeSocket = new ServerSocket(10049);          
		while(true)          
		{             
			Socket connectionSocket = welcomeSocket.accept();             
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));             
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());             
			clientSentence = inFromClient.readLine();             
			System.out.println("Received: " + clientSentence);             
			capitalizedSentence = clientSentence.toUpperCase() + '\n';             
			outToClient.writeBytes(capitalizedSentence);          
		
		} 
		
	}
	
	public String getMac()
	{
		InetAddress ip;
		StringBuilder sb = new StringBuilder();
		try 
		{
			
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());
	 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress();
	 
			System.out.print("Current MAC address : ");
	 
			
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			System.out.println(sb.toString());
	 
		} catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		} catch (SocketException e){
	 
			e.printStackTrace();
	 
		}
		return sb.toString();
	}
	
	public static void client () throws Exception
	{
		String sentence;   
		String modifiedSentence;   
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));   
		Socket clientSocket = new Socket("localhost", 10049);   
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());   
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   
		sentence = inFromUser.readLine();   
		outToServer.writeBytes(sentence + '\n');   
		modifiedSentence = inFromServer.readLine();   
		System.out.println("FROM SERVER: " + modifiedSentence);   
		clientSocket.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in)); 
		String input ="";
		
		BufferedReader br = new BufferedReader(new FileReader("D:/workspace/Proj2/src/file.txt"));
		   try {
		       StringBuilder sb = new StringBuilder();
		       String line = br.readLine();
		       String[] sline = line.split(" ");
		       
		           System.out.println(sline[0] + " " + sline[1]);
		           li.add(new FileDesc(sline[0],sline[1] ));
		       
		     
		   } finally {
		       br.close();
		   }
		 
		
		try {
			 input = inFromUser.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
