

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
 
public class Server {
    private DatagramSocket socket = null;
    private FileEvent event = null;
    private String sourceFilePath = "/Users/keertisekharsahoo/Documents/workspace/Lab5/src/images.jpeg";
    private String destinationPath = "/Users/keertisekharsahoo/Documents/file/";
    private String hostName = "172.16.220.173";
    public Server() {}
 
    public void createAndListenSocket() 
    {
    	try
    	{
    	socket = new DatagramSocket(6772);
    	InetAddress IPAddress = InetAddress.getByName(hostName);
    	File file = new File(sourceFilePath);
    	byte sendByte[] = new byte[(int)file.length()];
    	System.out.println("file length is "+file.length());
    	FileInputStream fin = null;
    	fin = new FileInputStream(file);
		fin.read(sendByte);
		System.out.println("file size is"+ sendByte.length);
		DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, IPAddress, 6773);
        socket.send(sendPacket);
        fin.close();
    	}
    	
    	catch (FileNotFoundException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
     
    public static void main(String[] args) {
        Server server = new Server();
        server.createAndListenSocket();
    }
}