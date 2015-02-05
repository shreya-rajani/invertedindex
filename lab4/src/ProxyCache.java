import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class ProxyCache {

	private static String CRLF = "\r\n";
	static long mainTimer=0;
	static long peerTimer=0;
	static long cacheTimer=0;
	static long webServerTimer=0;

	static HashMap<String, Integer> hm;
	/** Port for the proxy */
	private static int port;

	/** Socket for client connections */
	private static ServerSocket socket;

	/** Cache for Socket */
	private static Map<String, String> cache;

	/** Create the ProxyCache object and the socket */
	public static void init(int p) {
		port = p;
		try {
			socket = new ServerSocket(port);
			cache = new HashMap<String, String>(); // create cache storage
			hm = new HashMap<String, Integer>();

		} catch (IOException e) {
			System.out.println("Error creating socket: " + e);
			System.exit(-1);
		}
	}

	public static void handle(Socket client) {
		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;

		/**
		 * Process request. If there are any exceptions, then simply return and
		 * end this request. This unfortunately means the client will hang for a
		 * while, until it timeouts.
		 */

		// Read request
		// Uses cached version if detected
		System.out.println("\n1 ");
		request = readRequest(client);

		System.out.println("Does it really finish building request?");
		
		if(!request.getError()){
		if (cache.containsKey(request.getURL())) {
			System.out.println("Request is in cache.");

			String fCache = cache.get(request.getURL());
			File cacheFile = new File(fCache);
			int totalBytes = (int) cacheFile.length();
			byte[] cacheData = new byte[totalBytes];
			try {
				FileInputStream inFile = new FileInputStream(cacheFile);
				inFile.read(cacheData);

				DataOutputStream toClient = new DataOutputStream(
						client.getOutputStream());
				toClient.write(cacheData);

				cacheTimer = System.currentTimeMillis();
				System.out.println("Time to get from cache is: " + (mainTimer-cacheTimer));
				client.close();
				inFile.close();// added
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// if the server did not return an updated version
			// send the cached version to the client
			// sendResponseToClient(cache_response, client);
		}

		else {

			// Send request to peer
			System.out.println("\nPEER");

			Socket peer = null;
			String hashMapIP;
			int hashMapPort;
			for (Entry<String, Integer> entry : hm.entrySet()) {
				hashMapIP = entry.getKey();
				hashMapPort = entry.getValue();
				System.out.println("\n2. PEER");
				peer = sendRequestToPeer(request, hashMapIP, hashMapPort);

				// Get Response From Server and Forward to Client
				System.out.println("\n3. PEER ");
				response = getResponseFromPeer(peer);

			}
			if (response != null) {
				System.out.println("Response: " + response.toString() + "\n");

				System.out.println("\n4.PEER ");
				System.out.println("Got request from Peer");
				sendResponseToClient(response, client);//peer

				peerTimer = System.currentTimeMillis();
				System.out.println("Time to get from peer is: " + (mainTimer-peerTimer));
				
				System.out.println("\n5. PEER ");
				addRequestResponseToCache(request, response);

				try {
					client.close();
					peer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
				
				
			} else {

				// Sends Request to Server
				System.out.println("\n2 ");
				server = sendRequestToServer(request);

				// Get Response From Server and Forward to Client
				System.out.println("\n3 ");
				response = getResponseFromServer(server);
				System.out.println("Response: " + response.toString() + "\n");

				System.out.println("\n4 ");
				sendResponseToClient(response, client);
				webServerTimer = System.currentTimeMillis();
				System.out.println("Time to get from web server is: " + (mainTimer-webServerTimer));

				System.out.println("\n5 ");
				addRequestResponseToCache(request, response);

				try {
					client.close();
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		}
		else
			System.out.println("Didnt accept the request");
	}

	public static void handlePeer(Socket peer) {
//		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;

		/**
		 * Process request of peer and check in our own chache. If there are any
		 * exceptions, then simply return and end this request. This
		 * unfortunately means the client will hang for a while, until it
		 * timeouts.
		 */

		// Read request
		// Uses cached version if detected
		System.out.println("\n1 FROM PEER");
		request = readRequest(peer);

		if (cache.containsKey(request.getURL())) {
			System.out.println("Request is in cache.");

			String fCache = cache.get(request.getURL());
			File cacheFile = new File(fCache);
			int totalBytes = (int) cacheFile.length();
			byte[] cacheData = new byte[totalBytes];
			try {
				FileInputStream inFile = new FileInputStream(cacheFile);
				inFile.read(cacheData);

				DataOutputStream toPeer = new DataOutputStream(
						peer.getOutputStream());
				toPeer.write(cacheData);

				peer.close();
				inFile.close();// added
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// if the server did not return an updated version
			// send the cached version to the client
			// sendResponseToClient(cache_response, client);
		}

		else { // peer does not have it
			
			try {
				DataOutputStream toPeer = new DataOutputStream(
						peer.getOutputStream());
				toPeer.writeBytes("Y\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				peer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Read request */
	private static HttpRequest readRequest(Socket client) {
		try {
			BufferedReader fromClient = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			HttpRequest request = new HttpRequest(fromClient);// initialize the
																// request obj
			
			System.out.println("requedt completed");
			
			return request;

		} catch (IOException e) {
			System.out.println("Error reading from client: " + e);
			return null;

		}
	}

	/** Send request to server */
	private static Socket sendRequestToServer(HttpRequest request) {
		try {
			/** Open socket and write request to socket */
			System.out.println("Sending request to server...");
			Socket server = null;
			server = new Socket(request.getHost(), request.getPort());
			DataOutputStream toServer = new DataOutputStream(
					server.getOutputStream());
			toServer.writeBytes(request.toString());
			System.out.println("Finished sending request to server.\n");
			return server;

		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + request.getHost());
			System.out.println(e);
			return null;

		} catch (IOException e) {
			System.out.println("Error writing request to server: " + e);
			return null;

		}
	}

	/** Send request to peer */
	private static Socket sendRequestToPeer(HttpRequest request, String ip,
			int port) {
		try {
			/** Open socket and write request to socket */
			System.out.println("Sending request to peer...");
			Socket peer = null;
			
			System.out.println("IP: " + ip + "\nPort: " + port);
			peer = new Socket(ip, 9090);

			System.out.println("Socket created succ");
			
			DataOutputStream toPeer = new DataOutputStream(
					peer.getOutputStream());
			
			System.out.println("Is request null?");

			
			toPeer.writeBytes(request.toString());
			System.out.println("Finished sending request to peer.\n");
			return peer;

		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + request.getHost());
			System.out.println(e);
			return null;

		} catch (IOException e) {
			System.out.println("Error writing request to peer: " + e);
			return null;

		}
	}
	

	/** Read response and forward it to client */
	private static HttpResponse getResponseFromServer(Socket server) {
		try {
			DataInputStream fromServer = new DataInputStream(
					server.getInputStream());
			/** Fill in */
			HttpResponse response = new HttpResponse(fromServer);
			/** Fill in */
			return response;

		} catch (IOException e) {
			System.out.println("Error writing response to client: " + e);
			return null;

		}

	}

	/** Read response and forward it to peer */
	private static HttpResponse getResponseFromPeer(Socket server) {
		try {
			DataInputStream fromServer = new DataInputStream(
					server.getInputStream());
			/** Fill in */
			
			System.out.println("is server null?");
			
			String first = fromServer.readLine();
			
			System.out.println("or is there no data");
			
			HttpResponse response = null;
			if (!first.equals("Y")){
				
				response = new HttpResponse(first, fromServer);
			}
			
			return response;

		} catch (IOException e) {
			System.out.println("Error writing response to peer: " + e);
			return null;
		}
	}

	private static void sendResponseToClient(HttpResponse response,
			Socket client) {
		try {
			System.out.println("Sending Response to Client");
			DataOutputStream toClient = new DataOutputStream(
					client.getOutputStream());
			/** Fill in */

			System.out.println("response from peer is : " + response.toString());
			/** Write response to client. First headers, then body */
			System.out.println("Writing Headers to client...");
			
			toClient.writeBytes(response.toString());
			
			System.out.println("Writing Body to client...");
			System.out.println("Body: " + response.body);
			toClient.write(response.body);
			System.out.println("Done sending Response to Client");

		} catch (IOException e) {
			System.out.println("Error writing response to client: " + e);

		}
	}

	private static void sendResponseToPeer(HttpResponse response, Socket peer) {
		try {
			System.out.println("Sending Response to peer");
			DataOutputStream toClient = new DataOutputStream(
					peer.getOutputStream());
			/** Fill in */

			/** Write response to client. First headers, then body */
			System.out.println("Writing Headers to peer...");
			toClient.writeBytes(response.toString());
			/** Fill in */
			System.out.println("Writing Body to peer...");
			System.out.println("Body: " + response.body);
			toClient.write(response.body);
			System.out.println("Done sending Response to peer");

		} catch (IOException e) {
			System.out.println("Error writing response to peer: " + e);

		}
	}

	private static void addRequestResponseToCache(HttpRequest request,
			HttpResponse response) {
		/** Insert object into cache */
		/** Cache here is destructive, ie. stored in memory */
		System.out.println("adding to cache");

		String fileName = "file_" + System.currentTimeMillis();
		File fCache = new File(fileName);
		DataOutputStream fStream;
		try {
			fStream = new DataOutputStream(new FileOutputStream(fCache));
			fStream.writeBytes(response.toString());
			fStream.write(response.body);

			cache.put(request.getURL(), fileName);
//			System.out.println("Adding response to file complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedReader convertStringToBufferedReader(String str) {
		BufferedReader b = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(str.getBytes())));
		return b;
	}

	private class SendThread implements Runnable {

		SendingSocket ss = new SendingSocket();

		@Override
		public void run() {
			while (true) {
				ss.sendRequest();
			}
		}
	}

	private class ReceiveThread implements Runnable {

		ReceivingSocket rs = new ReceivingSocket();

		@Override
		public void run() {
			while (true) {
				rs.receiveResponse(myPort);
				hm = rs.GetHashMap();
//				System.out.println(hm);
			}
		}
	}

	int myPort = 0;

	private class ProxyThread implements Runnable {

		public void run() {

			System.out.println("Setting up socket.");
			try {
				myPort = 9090;

			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Need port number as argument");
				System.exit(-1);

			} catch (NumberFormatException e) {
				System.out.println("Please give port number as integer.");
				System.exit(-1);

			}

			init(myPort);
			System.out.println("Ready and listening.");

			/**
			 * Main loop. Listen for incoming connections and spawn a new thread
			 * for handling them
			 */
			Socket client = null;
			int c = 0;
			while (true) {
				try {
					client = socket.accept();
					mainTimer = System.currentTimeMillis();
					/** Fill in */
					System.out.println("\n-----------------------------------");
					System.out.println("- " + c + ": Got connection " + client);
					System.out.println("Clinet port number is:"
							+ client.getPort());
					if (client.getInetAddress().toString()
							.contains(client.getLocalAddress().toString())) {
						System.out.println("Request from my own browser.");
						handle(client);
					} else {
						System.out.println("Request from Peer");
						handlePeer(client);
					}
					System.out.println("End connection " + client + "\n\n");
					System.out.println("-----------------------------------\n");
					c++;

				} catch (IOException e) {
					System.out.println("Error reading request from client: "
							+ e);
					/**
					 * Definitely cannot continue processing this request, so
					 * skip to next iteration of while loop.
					 */
					continue;
				}
			}

		}

	}

	public void callingThread() {
		SendThread st = new SendThread();
		ReceiveThread rt = new ReceiveThread();
		ProxyThread pt = new ProxyThread();

		Thread t1 = new Thread(st);
		Thread t2 = new Thread(rt);
		Thread t3 = new Thread(pt);

		t1.start();
		System.out.println("Thread 1 started");
		t2.start();
		System.out.println("Thread 2 started");
		t3.start();
		System.out.println("Thread 3 started");
	}

	/** -------------------------------------------------- */
	/** Read command line arguments and start proxy */
	public static void main(String args[]) {
		new ProxyCache().callingThread();
		System.out.println("In main");
	}
}