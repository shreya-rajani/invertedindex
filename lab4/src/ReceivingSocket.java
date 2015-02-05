import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ReceivingSocket {
	HashMap<String, Integer> hm = new HashMap<String, Integer>();

	public void receiveResponse(int port) {

		MulticastSocket socket = null;
		DatagramPacket inPacket = null;
		byte[] inBuf = new byte[256];
		try {
			// Prepare to join multicast group
			socket = new MulticastSocket(5353);
			InetAddress address = InetAddress.getByName("224.0.0.254");
			socket.joinGroup(address);

			// while (true) {
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			String localIP = InetAddress.getLocalHost().toString();
			String[] ipLocal = localIP.split("/");
			System.out.println(ipLocal[1]);

			String ip = inPacket.getAddress().toString();
			String[] ipFromOutside = ip.split("/");

			int portNumber = inPacket.getPort();
			System.out.println(portNumber);

		if (!(ipFromOutside[1].equals(ipLocal[1]))) {

			//if(portNumber != port){ //it is not from the same port != same request
				System.out.println("IP Address is:" + ip);
				System.out.println("Port " + portNumber);

				StringBuilder sb = new StringBuilder();

				char ch = (char) inBuf[12];
				sb.append(ch); // c
				ch = (char) inBuf[13];
				sb.append(ch); // s

				int p = (char) inBuf[14];
				ch = (char) p;
				sb.append(ch);// 6
				p = (char) inBuf[15];
				ch = (char) p;
				sb.append(ch);// 2

				p = (char) inBuf[16];
				ch = (char) p;
				sb.append(ch);// 1
				ch = (char) inBuf[17];
				sb.append(ch);// -
				ch = (char) inBuf[18];
				sb.append(ch);// c
				ch = (char) inBuf[19];
				sb.append(ch); // a
				ch = (char) inBuf[20];
				sb.append(ch);// c
				ch = (char) inBuf[21];
				sb.append(ch);// h
				ch = (char) inBuf[22];
				sb.append(ch);// e
				String str = new String(sb);

				if (str.equalsIgnoreCase("cs621-cache")) {
					System.out.println("Matches the Q-Name");
						hm.put(ipFromOutside[1], inPacket.getPort());
						System.out.println("Adding to hash map: "+ ip +"and " + inPacket.getPort());
				}
				// }

				// String msg = new String(inBuf, 0, inPacket.getLength());
				// System.out.println("From " + inPacket.getAddress());

				// System.out.println(inPacket);
				// System.out.println(msg);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	public HashMap<String, Integer> GetHashMap() {

		return hm;
	}

	public static void main(String[] args) {
		ReceivingSocket sr = new ReceivingSocket();
		sr.receiveResponse(9090);
	}
}