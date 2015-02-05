import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Server 
{
	
	public void createAndListenSocket(int port) 
	{
		try 
		{
			DatagramSocket socket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			DatagramPacket initialPacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(initialPacket);
			receiveData = initialPacket.getData();
			
			String requestFile = new String(receiveData);
			System.out.println("The requested file is :"+requestFile);
			InetAddress clientIPAddress = initialPacket.getAddress();
			System.out.println("client's ip addres is "+clientIPAddress);
			int portClient = initialPacket.getPort();
			System.out.println("client's port is "+portClient);
			File file = new File(requestFile.trim());
			if(!file.exists())
			{
				System.out.println("The requested file doesn't exist with server...sorry.");
				return;
			}
			InputStream iStream = new FileInputStream(file);
			byte data[] = new byte[(int) file.length()];
			iStream.read(data);
			
			System.out.println("Total length of the file is " + file.length());
			
			System.out.println("file size is"+data.length);
			
			//System.exit(0);
			//System.out.println("Total size of the file is" + data.length);
			byte[] newByte = new byte[5120];
			int startIndex = 0;
			int packetLength = 5120;//packet length
			byte[] incomingACK = new byte[1];
			int mod;
			
			for (int i = 0; i < (((data.length) / (5120 - 1)) + 1); i++) 
			{

				System.out.println();
				System.out.println("Index Number: " + i);
				System.out.println("Sending file from server...");
				System.out.println("startindex is: " + startIndex
						+ " length is: " + packetLength);

				// storing the index number
				mod = i % 2;
				newByte[0] = (byte) mod;

				// checks for all the packets other than the last packet
				if (data.length - startIndex < packetLength - 1)
				{
					System.arraycopy(data, startIndex, newByte, 1, data.length - startIndex);
					startIndex = data.length - 1;
				}
				
				// checks for the last packet
				else 
				{
					System.arraycopy(data, startIndex, newByte, 1, packetLength - 1);
					startIndex += 5119;
				}

				// sends the packet to the client
				DatagramPacket serverPacket = new DatagramPacket(newByte,
						newByte.length, clientIPAddress, portClient);
				socket.send(serverPacket);
				
				double filesize = (double)file.length();
				double percent = (double)((startIndex/filesize)*100);
				System.out.println("Percentage:"+ percent + "%");
				
				DatagramPacket ACKPacketFromClient = new DatagramPacket(
							incomingACK, incomingACK.length);
				socket.setSoTimeout(2000);
				while (true) 
				{
					try 
					{
						socket.receive(ACKPacketFromClient);
						byte[] receiveACK = ACKPacketFromClient.getData();
						int ACKreceived = receiveACK[0];
						if(ACKreceived == -1)
						{
							ACKreceived =1;
						}
						if(ACKreceived != mod)
						{
							socket.send(serverPacket);
						}
						else
						{
							break;
						}
					}
					catch (SocketTimeoutException e) 
					{
						// sends the things to the client
						socket.send(serverPacket);
						System.out.println("Timeout");
					}
				}//end of while loop
				iStream.close();
			}
			socket.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		Server server = new Server();
		int port = Integer.parseInt(args[0]);
		server.createAndListenSocket(port);
	}
}