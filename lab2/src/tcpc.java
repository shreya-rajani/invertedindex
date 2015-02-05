import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
public class tcpc {
	public static void main(String args[]) {
		try {
			InetAddress addr = InetAddress.getByName("8.8.8.8");

			byte[] sendbyte = new byte[1024]; // 1 byte is 8 bits
			byte[] receivebyte = new byte[1024];
 
			int myLength = 0;
			
			sendbyte[0] = 0x3;
			myLength++;
			sendbyte[1] = 0x6F;
			myLength++;
			// 2nd
			sendbyte[2] = 0x01;
			myLength++;
			sendbyte[3] = 0x00;
			myLength++;
			// QDCOUNT
			sendbyte[4] = 0x00;
			myLength++;
			sendbyte[5] = 0x01;
			myLength++;
			// ANCOUNT
			sendbyte[6] = 0x00;
			myLength++;
			sendbyte[7] = 0x00;
			myLength++;
			// NSCOUNT
			sendbyte[8] = 0x00;
			myLength++;
			sendbyte[9] = 0x00;
			myLength++;
			// ARCOUNT
			sendbyte[10] = 0x00;
			myLength++;
			sendbyte[11] = 0x00;
			myLength++;
			// QNAME
			sendbyte[12] = 0x03;//
			myLength++;
			sendbyte[13] = 0x77;// w
			myLength++;
			sendbyte[14] = 0x77;// w
			myLength++;
			sendbyte[15] = 0x77;// w
			myLength++;
			sendbyte[16] = 0x03;//
			myLength++;
			sendbyte[17] = 0x63;// c
			myLength++;
			sendbyte[18] = 0x6e;// n
			myLength++;
			sendbyte[19] = 0x6e;// n
			myLength++;
			sendbyte[20] = 0x03;//
			myLength++;
			sendbyte[21] = 0x63;// c
			myLength++;
			sendbyte[22] = 0x6f;// o
			myLength++;
			sendbyte[23] = 0x6d;// m
			myLength++;
			sendbyte[24] = 0x00;//
			myLength++;
			// QTY
			sendbyte[25] = 0x00;
			myLength++;
			sendbyte[26] = 0x01;
			myLength++;
			// QCLAS
			sendbyte[27] = 0x00;
			myLength++;
			sendbyte[28] = 0x01;
			myLength++;
			
//			sendbyte2[0] = (byte) ((byte) myLength >> 8 & 0xFF);
//			sendbyte2[1] = (byte) ((byte) myLength & 0xFF);
			
			byte[] sendbyte2 = {0,0,0,0};
			sendbyte2[0] = 0x00;
			sendbyte2[1] = 0x00;
			sendbyte2[2] = 0x00;
			sendbyte2[3] = (byte) myLength;
			
			long timer = System.nanoTime();

			Socket socket = new Socket(addr, 53); 
			
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
          
//			outToServer.write(sendbyte2);
//			outToServer.write(sendbyte);
			//outToServer.wri
			//outToServer.writeInt(myLength);
			outToServer.write(sendbyte2);
			outToServer.write(sendbyte, 0, myLength);
			
			int length = inFromServer.readInt();

			byte [] receiver = new byte[length];
			byte [] rec = new byte[1024];
			
			  if (length > 0) {
			        inFromServer.readFully(receiver);
			    }
			System.out.println(receiver);
			
			//inFromServer.read(receivebyte);
			
			inFromServer.close();
			outToServer.close();
			
//			 for (int i = 0; i < receiver.length; i++) {
//			 String test = String.format("%02X", receiver[i]);
//			 System.out.println(test);
//			 }
			socket.close();

			long timer1 = System.nanoTime();
			/**
			 * End of time and closing of the
			 * socket------------------------------------------------
			 */

			long time = timer1 - timer;
			System.out.println(time);
			
			/**
			 * NOTE:
			 * 
			 * each number is 4 bits in wireshark 8 bits is 1 byte
			 * 
			 */
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
