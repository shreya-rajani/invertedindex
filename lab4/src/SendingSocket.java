import java.io.IOException;
import java.net.*;

public class SendingSocket {
	public static byte[] sendbyte;

	public static byte[] createQuery() {

		sendbyte = new byte[50]; // 1 byte is 8 bits
		// id
		sendbyte[0] = 0x03; // 1 byte = 8 bits
		sendbyte[1] = 0x06; // 1 byte = 8 bits

		// 2nd
		sendbyte[2] = 0x01;
		sendbyte[3] = 0x00;

		// QDCOUNT
		sendbyte[4] = 0x00;
		sendbyte[5] = 0x01;
		// ANCOUNT
		sendbyte[6] = 0x00;
		sendbyte[7] = 0x00;
		// NSCOUNT
		sendbyte[8] = 0x00;
		sendbyte[9] = 0x00;
		// ARCOUNT
		sendbyte[10] = 0x00;
		sendbyte[11] = 0x00;
		
		// QNAME
		sendbyte[12] = 0x63;// c
		sendbyte[13] = 0x73;// s
		sendbyte[14] = 0x36;// 6
		sendbyte[15] = 0x32;// 2
		sendbyte[16] = 0x31;// 1

		sendbyte[17] = 0x2d;// -
		sendbyte[18] = 0x63;// c
		sendbyte[19] = 0x61;// a
		sendbyte[20] = 0x63;// c
		sendbyte[21] = 0x68;// h
		sendbyte[22] = 0x65;// e
		sendbyte[23] = 0x00;

		sendbyte[24] = 0x09;//9
		sendbyte[25] = 0x00;//0

		// QCLASS
		sendbyte[26] = 0x00;//9
		sendbyte[27] = 0x01;//0

		return sendbyte;
	}

	public void sendRequest() {
		// Which port should we send to
		createQuery();
		int port = 5353;
		// Which address
		String group = "224.0.0.254";

		// Create the socket but we don't bind it as we are only going to send
		// data
		MulticastSocket s = null;
		while (true) {
			try {
				s = new MulticastSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Note that we don't have to join the multicast group if we are
			// only
			// sending data and not receiving
			// Fill the buffer with some data
			// byte buf[] = new byte[10];
			// for (int i = 0; i < buf.length; i++)
			// buf[i] = (byte) i;

			// Create a DatagramPacket
			DatagramPacket pack = null;
			try {
				pack = new DatagramPacket(sendbyte, sendbyte.length,
						InetAddress.getByName(group), port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			try {
				s.send(pack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// And when we have finished sending data close the socket
			s.close();
		}
	}
	
	public static void main(String[] args) {
		SendingSocket rs = new SendingSocket();
		rs.sendRequest();
	}
	
}