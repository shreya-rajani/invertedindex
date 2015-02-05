import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {
	private DatagramSocket socket = null;
	// private String hostName = "172.16.219.53";
	private String hostName = "10.0.0.3";

	public StringBuffer readFile() {

		File file = new File("demo.txt");
		System.out.println(file.getAbsolutePath());
		StringBuffer sb = new StringBuffer();
		sb = null;

		try (FileInputStream fis = new FileInputStream(file)) {

			System.out.println("Total file size to read (in bytes) : "
					+ fis.available());

			int content;
			while ((content = fis.read()) != -1) {
				sb = sb.append(content.toString());
			}
			System.out.print("Content is:" + sb);
			return sb;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}

	public void createAndListenSocket() {
		try {
			socket = new DatagramSocket(6772);
			InetAddress IPAddress = InetAddress.getByName(hostName);
			byte[] incomingData = new byte[1024];
			byte[] outgoingdata = new byte[1024];
			System.out.println("1.");
			String sb1 = readFile().toString();
			System.out.println("2.");
			outgoingdata = sb1.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(outgoingdata,
					outgoingdata.length, IPAddress, 6773);

			socket.send(sendPacket);

			System.out.println("Sending file from server...");

			DatagramPacket incomingPacket = new DatagramPacket(incomingData,
					incomingData.length);

			socket.receive(incomingPacket);

			String response = new String(incomingPacket.getData());

			System.out.println("Response from client:" + response);
			Thread.sleep(2000);
			System.exit(0);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.createAndListenSocket();
	}
}