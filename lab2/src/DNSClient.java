import java.net.*;

public class DNSClient {

	public static byte[] sendbyte;

	public static boolean isPointer(byte[] rec, int ctr) {

		String firstTwoBits = String
				.format("%8s", Integer.toBinaryString(rec[ctr] & 0xFF))
				.replace(' ', '0').substring(0, 2);
		if (firstTwoBits.equals("11")) {
			return true;
		}
		return false;
	}

	public static int getIndexFromPointer(byte[] rec, int ctr) {

		int index = 0;
		String firstTwoBits = String
				.format("%8s", Integer.toBinaryString(rec[ctr] & 0xFF))
				.replace(' ', '0').substring(0, 2);
		if (!(firstTwoBits.equals("11"))) {
			// ctr++; //next place where the pointer is pointing
			index = rec[ctr]; // got the place where it is poining
		}
		return index;
	}

	static int nctr = 0;

	public static int getDomainName(byte[] rec, int ctr, int num) {
		nctr = ctr;
		if (rec[ctr] == num && rec[ctr] != 00) {
			for (int i = ctr; i <= ctr + num; i++) {
				char str = (char) rec[i];
				System.out.print(str);
			}
			ctr = ctr + num + 1;
			System.out.print(".");
			num = rec[ctr];
			nctr = ctr;
			getDomainName(rec, ctr, num);
		}
		return nctr;
	}

	public static boolean cnameCheck(byte[] rec, int ctr) {

		return false;
	}

	public static byte[] createQuery(String queryType, String domainName) {
		String qType = queryType;
		String domain = domainName;
		String[] parts = domain.split("\\.");
		int chars, ctri = 0;

		sendbyte = new byte[1024]; // 1 byte is 8 bits
		// id
		sendbyte[0] = 0x03; // 1 byte = 8 bits
		sendbyte[1] = 0x6F; // 1 byte = 8 bits

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

		// System.out.println("hi");

		ctri = 12;
		// System.out.println(ctri);

		for (int i = 0; i < parts.length; i++) {
			chars = 0;
			// System.out.println("Array is: \n" + parts[i]);
			for (int j = 0; j < parts[i].length(); j++) {
				chars++;
			}
			sendbyte[ctri] = (byte) chars;
			ctri++;

			for (int k = 0; k < parts[i].length(); k++) {
				sendbyte[ctri] = (byte) (parts[i].charAt(k));
				// System.out.println("sendbyte["+ctri+"] = " +
				// (byte)(parts[i].charAt(k)));
				ctri++;
			}
		}
		// System.out.println(ctri);

		sendbyte[ctri] = 0x00;
		ctri++;

		// QTYPE
		if (qType.equalsIgnoreCase("A")) {
			sendbyte[ctri] = 0x00;
			ctri++;
			sendbyte[ctri] = 0x01;
			ctri++;
		}

		else if (qType.equalsIgnoreCase("NS")) {
			sendbyte[ctri] = 0x00;
			ctri++;
			sendbyte[ctri] = 0x02;
			ctri++;
		} else if (qType.equalsIgnoreCase("MX")) {
			sendbyte[ctri] = 0x00;
			ctri++;
			sendbyte[ctri] = 0x0F;
			ctri++;
		}

		// QCLASS
		sendbyte[ctri] = 0x00;
		ctri++;
		sendbyte[ctri] = 0x01;

		return sendbyte;
	}

	public static void main(String args[]) {
		try {

			long totalTime = 0, avg = 0, min = 1000000000, max = 10;
			String domain = args[0];
			String ipaddress = args[1];
			String qType = args[2];
			InetAddress addr = InetAddress.getByName(ipaddress);

			createQuery(qType, domain);

			byte[] receivebyte = new byte[1024];
			byte[] rec = new byte[1024];

			// System.out.println("counter"+ctri);

			int packetLossCtr = 0;

			for (int u = 0; u < 1; u++) {
				long timer = System.nanoTime();

				DatagramSocket socket = new DatagramSocket();
				DatagramPacket sender = new DatagramPacket(sendbyte,
						sendbyte.length, addr, 53);
				socket.send(sender);
				DatagramPacket receiver = new DatagramPacket(receivebyte,
						receivebyte.length);

				socket.setSoTimeout(1000); // set the timeout in millisecounds.
				try {
					socket.receive(receiver);
					rec = receiver.getData();
					// for(int i=0;i<rec.length;i++)
					// {
					// System.out.println(rec[i]);
					// }
					// System.exit(0);
					// if(rec[0] == sendbyte[0] && rec[1] == sendbyte[1]){
					// return true;
					// System.out.println("TRUE");
					// }
					// else
					// System.out.println("FALSE");

				} catch (SocketTimeoutException e) {
					// timeout exception.
					packetLossCtr++;
					System.out.println("Timeout reached!!! Packets lost : "
							+ packetLossCtr);
					socket.send(sender);
					socket.close();
				}

				long timer1 = System.nanoTime();

				long time = timer1 - timer;

				totalTime = totalTime + time;
				// System.out.println(time);

				if (min > time) {
					min = time;
				}

				if (max < time) {
					max = time;
				}

			}
			avg = totalTime / 100;
			// System.out.println("Average: " + avg);
			// System.out.println("Min:" + min);
			// System.out.println("Max: "+ max);
			// for printing the output
			// for (int i = 0; i < rec.length; i++) {
			// // System.out.println(rec[i]);
			// String test = String.format("%02X", rec[i]);
			// System.out.println(test);
			// }

			System.out.print("Query : " + rec[4] + rec[5]);
			System.out.print("  Answer RRs : " + rec[6] + rec[7]);
			System.out.print("  Authority RRs : " + rec[8] + rec[9]);
			System.out.println("  Additional RRs : " + rec[10] + rec[11]);

			System.out.println();

			System.out.println("Question Section");

			/**
			 * finding the value domain name. Part one for www
			 */

			int ctr = 12;
			int num = rec[ctr];

			// printing www
			ctr = getDomainName(rec, ctr, num);
			// ctr = ctr + num+1;
			//
			// num = rec[ctr]; // num==0x3

			System.out.print("        ");

			ctr++; // skipping the counter as after the domain, next 2 bytes are
					// 0

			if (rec[ctr] + rec[ctr + 1] == 0x1) {
				System.out.print(" A");
				ctr++;
			}

			else if (rec[ctr] + rec[ctr + 1] == 0x2) {
				System.out.print(" NS");
				ctr++;
			}

			else if (rec[ctr] + rec[ctr + 1] == 0xF) {
				System.out.print(" MX");
				ctr++;
			}

			System.out.print("        ");

			if (rec[ctr] + rec[ctr + 1] == 0x1) {
				System.out.print(" IN");
			}

			ctr = ctr + 2;

			System.out.println();
			ctr++;
			int nextctr = ctr;

			if (isPointer(rec, nextctr)) {
				nextctr++;
				int index = getIndexFromPointer(rec, nextctr);// : retrn index

				int n = rec[index];
				System.out.println();
				System.out.println("ANSWER SECTION");
				for (int i = 0; i < rec[7]; i++) {
				getDomainName(rec, index, n);
				System.out.println();
				}
			} else {
				ctr = getDomainName(rec, ctr, num);
			}
			ctr = ctr + 2;

			// String answer = String.format("%02X", rec[ctr] + rec[ctr + 1]);
			// System.out.println(answer);

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
