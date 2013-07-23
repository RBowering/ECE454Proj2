package Distributed;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Enumeration;

public class PeerRPC extends Thread{

	static List<File_Desc> flist = new ArrayList<File_Desc>();
    static List<Device> dlist = new ArrayList<Device>();
    static String ourDev;
     //static String basePath = "/home/rbowerin/ece454/Proj2/src/";
    static String basePath = "/Users/ishashori/4A-S13/JavaApplication2/src/Distributed/";
    private static String downloadPath;
    private final BlockingQueue q;
    public PeerRPC(BlockingQueue q)
	{ 
	    this.q = q;
		
	}
   
    public void run () {
       findDevice();
	
		Scanner br = null;
        
        try {
	    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        br = new Scanner(new FileReader(basePath
                + "file.txt"));
            StringBuilder sb = new StringBuilder();
            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] sline = line.split(" ");

                
                flist.add(new File_Desc(sline[0], sline[1]));

            }
			br.close();
            while (true) 
			{
                String in[];
				System.out.println("Our device: "+ourDev);
				System.out.println("Buffer empty: "+q.isEmpty());
                while (q.isEmpty()) 
				{
                    continue;
                }
				in = ((String)q.remove()).split(" ");
                if (in[0].equals("open")) {
                    open(in[1], in[2]);

                } else if (in[0].equals("read")) {

                    int parseInt1 = Integer.parseInt(in[3]);
                    int parseInt2 = Integer.parseInt(in[2]);
                    char buff[] = new char[parseInt1 + parseInt2 + 1];
                    read(in[1], buff, Integer.parseInt(in[2]), Integer.parseInt(in[3]));
                } else if (in[0].equals("write")) {

                    //int parseInt = Integer.parseInt(in[3]);
                    //char buff[] = new char[parseInt];
                    String writeIn = "";
                    char write[];
                    writeIn = inFromUser.readLine();
                    write = writeIn.toCharArray();
                    int writeSize = write.length;
                    char wr[] = new char[writeSize + Integer.parseInt(in[2])];
                    int offset = Integer.parseInt(in[2]);
                    System.arraycopy(write, 0, wr, 0, write.length);
                    write(in[1], wr, Integer.parseInt(in[2]), writeSize);
                } else if (in[0].equals("close")) {
                    close(in[1]);

                } else if (in[0].equals("remove")) {
                    remove(in[1]);

                } else if (in[0].equals("insert")) {
                    insert(in[1]);

                } else if (in[0].equals("delete")) {
                    delete(in[1]);

                }
            }
        } 
	catch (Exception e)
	{

	}
	finally {
            br.close();
        }

    }

    public static int open(String fileName, String operation) {
        try {
            //TODO: find if the file is local or remote.....

            Scanner file = new Scanner(new FileReader(basePath
                    + fileName));
            if (operation.equals("r")) {
                for (int i = 0; i < flist.size(); i++) {
                    if (flist.get(i).name.equals(fileName)) {
                        if (flist.get(i).loc.equals(ourDev)) {
                            System.out.println("GOT THE FILE: " + fileName);
                            flist.get(i).fileIn = new FileReader(basePath
                                    + fileName);
                            flist.get(i).fIn = 1;
                        } else {
                            for (int j = 0; j < dlist.size(); j++) {

                                if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                                    Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "open " + fileName + " " + operation);
                                }
                            }
                        }
                    }
                }
            }
            if (operation.equals("w")) {
                for (int i = 0; i < flist.size(); i++) {
                    if (flist.get(i).name.equals(fileName)) {
                        if (flist.get(i).loc.equals(ourDev)) {
                            System.out.println("GOT THE FILE: " + fileName);
                            try {
                                flist.get(i).fileOut = new FileWriter(basePath
                                        + fileName);
                                flist.get(i).fOut = 1;
                            } catch (IOException ex) {
                                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            for (int j = 0; j < dlist.size(); j++) {

                                if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                                    Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "open " + fileName + " " + operation);
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            //if it enters this exception then we know the file is not local..soo look in to 
            //the fileList to find the file location
            System.out.println("FILE DOES NOT EXIST" + fileName);
            return -1;
        }
        return 0;
    }

    private static void read(String fileName, char[] charBuff, int offset, int buffsize) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                if (flist.get(i).loc.equals(ourDev)) {
                    try {
                        flist.get(i).fileIn.read(charBuff, offset, buffsize);
                        System.out.println(charBuff);
                    } catch (IOException ex) {
                        Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    for (int j = 0; j < dlist.size(); j++) {

                        if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                            Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "read " + fileName + " " + offset + " " + buffsize);
                        }
                    }
                }
            }
        }

    }

    private static void write(String fileName, char[] charBuff, int offset, int buffsize) {
        //look for input and determine its size
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                if (flist.get(i).loc.equals(ourDev)) {
                    try {
                        flist.get(i).fileOut.write(charBuff, offset, buffsize);
                        System.out.println(charBuff);

                    } catch (IOException ex) {
                        Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    for (int j = 0; j < dlist.size(); j++) {

                        if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                            Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "write " + fileName + " " + offset);
                        }
                    }
                }
            }
        }
    }
    
    private static void save(String fileName, char[] charBuff, int offset, int buffsize) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                if (flist.get(i).loc.equals(ourDev)) {
                    try {
                        flist.get(i).fileOut.flush();
                    } catch (Exception ex) {
                        File file = new File(downloadPath + fileName);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                fw.write(charBuff, offset, buffsize);
                                fw.flush();
                                insert(file.getName());
                                fw.close();
                            } catch (IOException ex1) {
                                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        }
                        System.out.print("Error!! Saving file so a copy is saved in the download folder");

                    }
                } else {
                    for (int j = 0; j < dlist.size(); j++) {

                        if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                            Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "save " + fileName);
                        }
                    }

                }
            }
        }
    }


    private static void close(String fileName) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                if (flist.get(i).loc.equals(ourDev)) {
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
                } else {
                    for (int j = 0; j < dlist.size(); j++) {

                        if (flist.get(i).loc.equals(dlist.get(j).dev)) {
                            Client c = new Client(dlist.get(j).ip, dlist.get(j).port, "close " + fileName);
                        }
                    }
                }
            }
        }
    }

    private static void remove(String fileName) {
        String toKeep[] = new String[flist.size()];
        int index = 0;
        try {
            Scanner br = new Scanner(new FileReader(basePath
                    + "file.txt"));


            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] sline = line.split(" ");

                if (!sline[0].equals(fileName)) {
                    toKeep[index] = line;
                    index++;
                }

            }
            FileWriter fw = new FileWriter(basePath
                    + "file.txt");
            fw.write("", 0, 0);
            fw.close();
            fw = new FileWriter(basePath
                    + "file.txt", true);
            char toKeepc[][] = new char[flist.size()][50];
            for (int i = 0; i < index; i++) {
                toKeepc[i] = toKeep[i].toCharArray();
                fw.write(toKeepc[i], 0, toKeepc[i].length);
            }
            fw.close();
            System.out.println("Done removing");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static void insert(String fileName) {
        try {

            if (open(fileName, "r") != 0) {

                return;
            }
            close(fileName);
            FileWriter fw = new FileWriter(basePath
                    + "file.txt", true);
            //FIND UR IP...

            fileName = "\n" + fileName + " " + ourDev;
            fw.write(fileName.toCharArray(), 0, fileName.length());

            fw.close();
            flist.add(new File_Desc(fileName, ourDev));
            System.out.println("Done inserting");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void delete(String fileName) {
        for (int i = 0; i < flist.size(); i++) {
            if (flist.get(i).name.equals(fileName)) {
                if (flist.get(i).loc.equals(ourDev)) {

                    File file = new File(basePath + fileName);
                    if (file.delete()) {
                        System.out.println("DELETE filename: " + fileName);
                    }
                    for (int j = 0; j < flist.size(); j++) {
                        if (flist.get(j).name.equals(fileName)) {
                            flist.remove(j);
                        }
                    }

                    remove(fileName);
                } else {
                    for (int k = 0; k < dlist.size(); k++) {

                        if (flist.get(i).loc.equals(dlist.get(k).dev)) {
                            Client c = new Client(dlist.get(k).ip, dlist.get(k).port, "delete " + fileName);
                        }
                    }
                }
            }
        }
    }

    private static void findDevice() {
        InetAddress ip;
InetAddress addr = null;
String sp[];
        try {
            
    NetworkInterface i = NetworkInterface.getByName("eth0");

    if (i != null) {

        Enumeration<InetAddress> iplist = i.getInetAddresses();

        

        while (iplist.hasMoreElements()) {
            InetAddress ad = iplist.nextElement();
            byte bs[] = ad.getAddress();
            if (bs.length == 4 && bs[0] != 127) {
                addr = ad;
                // You could also display the host name here, to 
                // see the whole list, and remove the break.
                break;
            }
        }
	}
        if (addr != null) {
            System.out.println( addr.getCanonicalHostName() );
        
	    
		String ip_s = addr.getHostAddress();
		System.out.println("IP   " + ip_s);
            Scanner br = new Scanner(new FileReader(basePath
                    + "devices.txt"));

            StringBuilder sb = new StringBuilder();
            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] sline = line.split(" ");

                System.out.println(sline[0] + " " + sline[1] + " " + sline[2]);
                dlist.add(new Device(sline[0], sline[1], sline[2], ip_s.equals(sline[1]) ? true : false));
                if (ip_s.equals(sline[1])) {
                    ourDev = sline[0];
                }
            }
	}
	else
	{
		ip = InetAddress.getLocalHost();
	
            
            sp = ip.toString().split("/");
	
            System.out.println("IP   " + sp[1].toString());
            Scanner br = new Scanner(new FileReader(basePath
                    + "devices.txt"));

            StringBuilder sb = new StringBuilder();
            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] sline = line.split(" ");

                System.out.println(sline[0] + " " + sline[1] + " " + sline[2]);
                dlist.add(new Device(sline[0], sline[1], sline[2], sp[1].toString().equals(sline[1]) ? true : false));
                if (sp[1].toString().equals(sline[1])) {
                    ourDev = sline[0];
                }
            }
	}
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
	catch (SocketException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}
