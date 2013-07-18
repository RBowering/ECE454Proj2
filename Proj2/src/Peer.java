package Distributed;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Peer {

    static List<File> flist = new ArrayList<File>();

    /*public static void server () throws Exception
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
		
     }*/
    /*public String getMac()
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
     }*/
    /*public static void client () throws Exception
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
     }*/
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Server server = new Server();
        Client client = new Client();



        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        Scanner br = new Scanner(new FileReader("/Users/ishashori/4A-S13/JavaApplication2/src/Distributed/"
                + "file.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] sline = line.split(" ");

                System.out.println(sline[0] + " " + sline[1]);
                flist.add(new File(sline[0], sline[1]));

            }
            while (true) {
                input = inFromUser.readLine();
                String in[];
                in = input.split(" ");
                if (in[0].equals("open")) {
                    open(in[1], in[2]);

                } else if (in[0].equals("read")) {

                    int parseInt = Integer.parseInt(in[3]);
                    char buff[] = new char[parseInt];
                    read(in[1], buff, Integer.parseInt(in[2]), Integer.parseInt(in[3]));
                } else if (in[0].equals("write")) {

                    //int parseInt = Integer.parseInt(in[3]);
                    //char buff[] = new char[parseInt];
                    String writeIn = "";
                    char write[];
                    writeIn = inFromUser.readLine();
                    write = writeIn.toCharArray();
                    int writeSize = write.length;
                    write(in[1], write, Integer.parseInt(in[2]), writeSize);
                }
                if (in[0].equals("close")) {
                    close(in[1]);

                }
            }
        } finally {
            br.close();
        }

    }

    public static void open(String fileName, String operation) {
        try {
            //TODO: find if the file is local or remote.....
            Scanner file = new Scanner(new FileReader("/Users/ishashori/4A-S13/JavaApplication2/src/Distributed/"
                    + fileName));
            if (operation.equals("r")) {
                for (int i = 0; i < flist.size(); i++) {
                    if (flist.get(i).name.equals(fileName)) {
                        System.out.println("GOT THE FILE: " + fileName);
                        flist.get(i).fileIn = new FileReader("/Users/ishashori/4A-S13/JavaApplication2/src/Distributed/"
                                + fileName);
                        flist.get(i).fIn = 1;

                    }
                }
            }
            if (operation.equals("w")) {
                for (int i = 0; i < flist.size(); i++) {
                    if (flist.get(i).name.equals(fileName)) {
                        System.out.println("GOT THE FILE: " + fileName);
                        try {
                            flist.get(i).fileOut = new FileWriter("/Users/ishashori/4A-S13/JavaApplication2/src/Distributed/"
                                    + fileName,true);
                            flist.get(i).fOut = 1;
                        } catch (IOException ex) {
                            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            //if it enters this exception then we know the file is not local..soo look in to 
            //the fileList to find the file location
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void read(String fileName, char[] charBuff, int offset, int buffsize) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                try {
                    flist.get(i).fileIn.read(charBuff, offset, buffsize);
                    System.out.println(charBuff);
                } catch (IOException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private static void write(String fileName, char[] charBuff, int offset, int buffsize) {
        //look for input and determine its size
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                try {
                    flist.get(i).fileOut.write(charBuff, offset, buffsize);
                    System.out.println(charBuff);

                } catch (IOException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void close(String fileName) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                try {
                    if (flist.get(i).fIn == 1) {
                        flist.get(i).fileIn.close();
                    }
                    if (flist.get(i).fOut == 1) {
                        flist.get(i).fileOut.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
