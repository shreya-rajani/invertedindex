import java.io.*;
import java.net.*;
import java.util.HashMap;

public class SocketReceive 
{
	static HashMap<InetAddress, Integer> map = new HashMap<InetAddress,Integer>();
	public void receiveResponse()
	{
		//HashMap<InetAddress, Integer> map = new HashMap<InetAddress,Integer>();
		System.out.println("Inside receiving socket");
		System.out.println("Thread is"+ Thread.currentThread());
	    MulticastSocket socket = null;
	    DatagramPacket inPacket = null;
	    byte[] inBuf = new byte[256];
	    byte[] received = new byte[256];
	    //HashMap<InetAddress, Integer> hashMap = new HashMap<InetAddress, Integer>();
	    
	    try 
	    {
	      //Prepare to join multicast group
	      socket = new MulticastSocket(5353);
	      InetAddress address = InetAddress.getByName("224.0.0.253");
	      System.out.println(address);
	      socket.joinGroup(address);
	 
	      while (true) 
	      {
	        inPacket = new DatagramPacket(inBuf, inBuf.length);
	        socket.receive(inPacket);
	        received = inPacket.getData();
	    	
	        int index =12;
	    	StringBuilder sb = new StringBuilder();
	    	
	    	char ch = (char) received[12];
	    	sb.append(ch); //c
	    	ch = (char) received[13];
	    	sb.append(ch); //s
    	
	    	int p = (char)received[14]; 
	    	ch = (char)p;
	    	sb.append(ch);//6
	    	//System.out.println("Read 15");
	    	p = (char)received[15]; 
	    	ch = (char)p;
	    	sb.append(ch);//2
	    	
	    	p = (char)received[16]; 
	    	ch = (char)p;
	    	sb.append(ch);//1
	    	ch = (char) received[17];
	    	sb.append(ch);//-
	    	ch = (char) received[18];
	    	sb.append(ch);//c
	    	ch = (char) received[19];
	    	sb.append(ch); //a
	    	ch = (char) received[20];
	    	sb.append(ch);//c
	    	ch = (char) received[21];
	    	sb.append(ch);//c
	    	ch = (char) received[22];
	    	sb.append(ch);//h
	    	ch = (char) received[23];
	    	sb.append(ch);//e
	    	String str = new String(sb);
    	
	    	if(str.trim().equals("cs621-cache"))
	    	{
	    		System.out.println("Service types are same");
	    		InetAddress addr = inPacket.getAddress();
		    	//String ip = new String();
		    	//String ip = inPacket.getAddress();
		    	int port1 = inPacket.getPort();
		    	map.put(addr, port1);
	    	}
	    	else
	    	{
	    		System.out.println("Service types are different");
	    	}
	    	
	    	System.out.println("From " + inPacket.getAddress());
	    	//System.out.println("Port " + inPacket.getPort());
	    	System.out.println("Map size is"+map.size());
	      	}
	    } 
	    catch (IOException ioe)
	    {
	    	System.out.println(ioe);
	    }
	    //System.out.println("before returing map"+map.size());
	    //return map;
	}
	
	public HashMap<InetAddress,Integer> getHashMap()
	{
		//System.out.println("inside getHashmap");
		return map;
	}

	
	public static void main(String[] args)
	{
		//SocketReceive sr = new SocketReceive();
		//SocketReceive.RunnableType t = sr.new RunnableType();
		//t.start();
		//sr.receiveResponse();
	}
}
