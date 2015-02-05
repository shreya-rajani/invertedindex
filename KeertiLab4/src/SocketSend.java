import java.io.*;
import java.net.*;

public class SocketSend 
{
	public void sendResponse() throws IOException
	{
		System.out.println("inside sendResponse method");
		System.out.println("Thread is" + Thread.currentThread());
		MulticastSocket multiSocket = null;
	    //DatagramSocket socket = null;
	    DatagramPacket outPacket = null;
	    byte[] sendByte = new byte[512];
	    int PORT = 5353;
	    
	    sendByte[0] = 0x23;//1st
		sendByte[1] = 0x6F;
	
		//2nd
		sendByte[2] = 0x01;
		sendByte[3] = 0x00;
		//QDCOUNT
		sendByte[4] = 0x00;
		sendByte[5] = 0x01;
		//ANCOUNT
		sendByte[6] = 0x00;
		sendByte[7] = 0x00;
		//NSCOUNT
		sendByte[8] = 0x00;
		sendByte[9] = 0x00;
		//ARCOUNT
		sendByte[10] = 0x00;
		sendByte[11] = 0x00;
		//QNAME
		sendByte[12] = 0x63;
		sendByte[13] = 0x73;
		sendByte[14] = 0x36;
		sendByte[15] = 0x32;
		sendByte[16] = 0x31;
		sendByte[17] = 0x2d;
		sendByte[18] = 0x63;
		sendByte[19] = 0x61;
		sendByte[20] = 0x63;
		sendByte[21] = 0x68;
		sendByte[22] = 0x65;
		sendByte[23] = 0x00;
		//QTYPE
		sendByte[24] = 0x00;
		sendByte[25] = 0x0c;
		//QCLASS
		sendByte[26] = 0x00;
		sendByte[27] = 0x01;
		multiSocket = new MulticastSocket();
		while (true) 
		{
    	InetAddress address = InetAddress.getByName("224.0.0.253");
        outPacket = new DatagramPacket(sendByte, sendByte.length, address, PORT);
       
        multiSocket.send(outPacket);
        System.out.println("Server is sending");
        //System.out.println("Server sends : " + ip);
        try 
        {
          Thread.sleep(1000);
        }
        catch (InterruptedException ie) 
        {
        }
      } 
     
  }
}

  


	

 



