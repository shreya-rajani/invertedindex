import java.io.*;
import java.net.*;

public class Client 
{
	public void createConnection(String IPserver, int portServer, String fileName) throws IOException
	{
	
			InetAddress ServerIP = InetAddress.getByName(IPserver);
			DatagramSocket socket = new DatagramSocket();
			byte[] initialByte = new byte[1024];
			initialByte = fileName.getBytes();
			DatagramPacket initialPacket = new DatagramPacket(initialByte, initialByte.length, ServerIP, portServer);
			System.out.println("Sending initial packet for the file request...");
			socket.send(initialPacket);
			
			File file = new File(fileName);
			FileOutputStream toFile = new FileOutputStream(file);
			int clientCTR = 0;
			int clientACK=0;
			
				while (true) 
				{
					byte[] receivedData = new byte[5120];
					byte[] dataPacket = new byte[5119];
					DatagramPacket incomingPacket = new DatagramPacket(receivedData, receivedData.length);
					socket.receive(incomingPacket);
					receivedData = incomingPacket.getData();
					//check the ack from server
					clientACK = receivedData[0];
					if (clientACK == clientCTR % 2) 
					{
						byte[] newdata = new byte[receivedData.length - 1];
						System.arraycopy(receivedData, 1, newdata, 0,(receivedData.length - 1));
						System.out.println("Recieved packet: " + clientACK);
						toFile.write(newdata);
						byte[] ACKnumber = new byte[1];
						ACKnumber[0] = (byte) clientACK;

						// send ACK
						System.out
								.println("Sending ACK to server for the packet: "
										+ clientACK);
						DatagramPacket ACKPacket = new DatagramPacket(
								ACKnumber, ACKnumber.length,
								incomingPacket.getAddress(),
								incomingPacket.getPort());
						socket.send(ACKPacket);
						clientCTR = (clientCTR + 1) % 2;
							
					} 
					else 
					{
						System.out.println("Unable to receive the packet");
						System.out.println("Sending the previous ACK: "
								+ (clientCTR + 1) % 2);
						byte[] ACKnumber = new byte[1];
						ACKnumber[0] = (byte) ((clientCTR - 1) % 2);
						DatagramPacket ACKPacket1 = new DatagramPacket(ACKnumber, ACKnumber.length, ServerIP,portServer);//
						socket.send(ACKPacket1);
					}
				}//while loop ends here 
			}

	public static void main(String[] args) throws IOException
	{
		String IPserver = args[0];
		int portServer = Integer.parseInt(args[1]);
		String fileName = args[2];
		Client client = new Client();
		client.createConnection(IPserver, portServer, fileName);
	}
}
