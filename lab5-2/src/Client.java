import java.io.*;
import java.net.*;

public class Client {
	private DatagramSocket socket = null;
	DataOutputStream dos;

//	public Client() {
//	}

	public void createConnection(String IPserver, int portServer, String fileName) {

	//public void createConnection(){
		try {
			//String hostName = "172.16.218.205";
//			InetAddress IPAddress = InetAddress.getByName(host);
//			socket = new DatagramSocket(portServer); // clients port
//			System.out.println("Socket is created.....");
			byte[] incomingData = new byte[51200];
			
//			//String fileName = "/Users/shreyarajani/Documents/Lab5_Folder/Happy.mp4";
//			File file = new File(fileName);
			

//			String req = request;
//
//			DatagramPacket reqPacket = new DatagramPacket(req.getBytes(),
//					req.length(), IPAddress, 6773);
//			socket.send(reqPacket);
//
//			byte[] requestarray = new byte[51200];
//			DatagramPacket requestcheck = new DatagramPacket(requestarray,
//					requestarray.length);
//			socket.receive(requestcheck);

			//if (requestarray != null) {
			
			InetAddress ServerIP = InetAddress.getByName(IPserver);
			socket = new DatagramSocket();
			byte[] initialByte = new byte[1024];
			initialByte = fileName.getBytes();
			DatagramPacket initialPacket = new DatagramPacket(initialByte, initialByte.length, ServerIP, portServer);
			System.out.println("Sending initial packet for the file request...");
			socket.send(initialPacket);
			//System.exit(0);
			File file = new File(fileName);
			dos = new DataOutputStream(new FileOutputStream(file));
			int clientCTR = 0;
			
			
			
			
			
			
			

			int clientACK=0;
				while (true) 
				{
					System.out.println("inside while");
					DatagramPacket incomingPacket = new DatagramPacket(
							incomingData, incomingData.length);
					//socket.setSoTimeout(2500);
					try 
					{
						socket.receive(incomingPacket);
						// incomingPacket.getPort();
						byte[] receiveData = incomingPacket.getData();

						clientACK = receiveData[0];

						if (clientACK == clientCTR % 2) 
						{
							byte[] newdata = new byte[receiveData.length - 1];
							System.arraycopy(receiveData, 1, newdata, 0,
									(receiveData.length - 1));
							System.out.println("Recieved packet: " + clientACK);
							dos.write(newdata);
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
							// incomingPacket.getPort();
						} 
						else 
						{
							System.out.println("Unable to receive the packet");
							System.out.println("Sending the previous ACK: "
									+ (clientCTR + 1) % 2);
							byte[] ACKnumber = new byte[1];
							ACKnumber[0] = (byte) ((clientCTR - 1) % 2);
							DatagramPacket ACKPacket1 = new DatagramPacket(
									ACKnumber, ACKnumber.length, ServerIP,
									portServer);//
							socket.send(ACKPacket1);
						}

					} 
					catch (SocketTimeoutException e) 
					{
//						
						byte[] ACKnumber = new byte[1];
						ACKnumber[0] = (byte) clientACK;
						DatagramPacket ACKPacket = new DatagramPacket(
								ACKnumber, ACKnumber.length,
								ServerIP,
								portServer);
						socket.send(ACKPacket);
						System.out.println("Waiting");
						
					}
				}
			//}

//			else if (requestarray[0] == 0) {
//				System.out.println("Requested file not found");
//			}
			 
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//dos.close();
		socket.close();
	}

	public static void main(String[] args) {
		String IPserver = args[0];
		int portServer = Integer.parseInt(args[1]);
		String fileName = args[2];
		Client client = new Client();
		client.createConnection(IPserver, portServer, fileName);
		//client.createConnection();
	}
}
