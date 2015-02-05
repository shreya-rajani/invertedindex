import java.io.*;
import java.net.*;

public class Client {
	private DatagramSocket socket = null;

	public Client() {
	}

	public void createConnection() {

		try {
			socket = new DatagramSocket(6773);
			byte[] incomingData = new byte[1024];
			while (true) {

				DatagramPacket incomingPacket = new DatagramPacket(
						incomingData, incomingData.length);
				socket.receive(incomingPacket);
				byte[] incomingdata = incomingPacket.getData();

				byte[] outgoingData = new byte[1024];

				InetAddress IPAddress = incomingPacket.getAddress();

				int port = incomingPacket.getPort();

				System.out.println("Got the packet...");
				System.out.println(incomingdata.toString());

				DatagramPacket outgoingPacket = new DatagramPacket(outgoingData,
						outgoingData.length, IPAddress, port);
				socket.send(outgoingPacket);
				Thread.sleep(3000);
				System.exit(0);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.createConnection();
	}
}