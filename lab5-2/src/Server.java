import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Server {
	private DatagramSocket socket = null;
	//private String sourceFile = "/Users/shreyarajani/MS-Sem1/SD/prog/lab5-2/src/Happy.mp4";
	//private String hostName = "172.16.218.205";

//	public Server() {
//	}

	public void createAndListenSocket(int port) {
		try {
			socket = new DatagramSocket(port);// severs port
//			//InetAddress IPAddress = InetAddress.getByName(hostName);
//
//			byte[] requestarray = new byte[51200];
//			// DatagramPacket request = new DatagramPacket(requestarray,
//			// requestarray.length);
//			// socket.receive(request);
//
//			// String reqString = request.toString();
//			// CHECK
//			File file = new File(sourceFile);
//
//			// if (file != null) {
			
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
			//System.exit(0);
			
			File file = new File(requestFile.trim());
			//File file = new File("/Users/shreyarajani/MS-Sem1/SD/prog/lab5-2/src/state.pdf");
			//System.out.println("file is"+file.length());
			if(!file.exists())
			{
				System.out.println("The requested file doesn't exist with server...sorry.");
				return;
			}
//			System.out.println("absolute path is"+file.getAbsolutePath());
//			InputStream iStreamF = new FileInputStream(file);
//			System.out.println("file length is"+file.length());
//			byte[] fileData = new byte[(int) file.length()];
//		
////			FileInputStream fInput = null;
////			fInput = new FileInputStream(file);
////			fInput.read(data);
//			iStreamF.read(fileData);
//			System.out.println("absolute path is"+file.getAbsolutePath());
//			System.out.println("Total length of the file is " + fileData.length);
			//System.out.println("sdads"+file);
			byte data[] = new byte[(int) file.length()];
			System.out.println("Total length of the file is " + file.length());
			FileInputStream fInput = null;
			fInput = new FileInputStream(file);
			fInput.read(data);
			System.out.println("file size is"+data.length);
			
			//System.exit(0);
			//System.out.println("Total size of the file is" + data.length);
			byte[] newByte = new byte[51200];
			int startIndex = 0;
			int length1 = 51200;
			byte[] incomingACK = new byte[1];
			int mod;

			if (data.length > (51200 - 1)) 
			{
				System.out.println("inside if");
				for (int i = 0; i < (((data.length) / (51200 - 1)) + 1); i++) 
				{

					System.out.println();

					System.out.println("Index Number: " + i);
					System.out.println("Sending file from server...");
					System.out.println("startindex is: " + startIndex
							+ " length is: " + length1);

					// stroring the index number
					mod = i % 2;
					newByte[0] = (byte) mod;

					// checks for all the packets other then the last packet
					if (data.length - startIndex < length1 - 1) {

						System.arraycopy(data, startIndex, newByte, 1,
								data.length - startIndex);
						startIndex = data.length - 1;
					}

					// checks for the last packet
					else {
						System.arraycopy(data, startIndex, newByte, 1,
								length1 - 1);
						startIndex += 51199;
					}

					// sends the things to the client
					DatagramPacket serverPacket = new DatagramPacket(newByte,
							newByte.length, clientIPAddress, portClient);
					socket.send(serverPacket);
					double filesize = (double)file.length();
					double percent = (double)((startIndex/filesize)*100);
					System.out.println("Percentage:"+ percent + "%");
					
					while (true) 
					{
						DatagramPacket ACKPacketFromClient = new DatagramPacket(
								incomingACK, incomingACK.length);

						socket.setSoTimeout(500);
						try 
						{
							socket.receive(ACKPacketFromClient);
							byte[] receiveACK = ACKPacketFromClient.getData();
							int ACKreceived = receiveACK[0];

							if (ACKreceived == mod) {
								System.out.println("Received ");
								break;
							}

							else 
							{
								socket.send(serverPacket);
							}
						}
						catch (SocketTimeoutException e) 
						{
							// sends the things to the client
							socket.send(serverPacket);
							System.out.println("Timeout");
						}
					}
					fInput.close();
				}
			} 
			else 
			{
				System.out.println("only one packet is being sent");
				DatagramPacket sendPacket = new DatagramPacket(data,
						data.length, clientIPAddress, portClient);
				socket.send(sendPacket);
			}
			fInput.close();
			socket.close();
			// }

			// else {
			// byte[] nackarray = new byte[51200];
			// DatagramPacket nack = new DatagramPacket(nackarray,
			// nackarray.length, IPAddress, 6773);
			// socket.send(nack);
			// }
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Server server = new Server();
		int port = Integer.parseInt(args[0]);
		server.createAndListenSocket(port);
	}
}