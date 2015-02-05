import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class ProxycacheError3 {

	private static String CRLF = "\r\n";
	static HashMap<String, Integer> hm = new HashMap<String, Integer>();

	/** Port for the proxy */
	private static int port;

	/** Socket for client connections */	
	private static ServerSocket socket;

	/** Cache for Socket */
	private static Map cache;

	/** Create the ProxyCache object and the socket */
	public static void init(int p) {
		port = p;
		try {
			socket = new ServerSocket(port);
			/** Fill in */
			cache = new HashMap(); // create cache storage

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
		// Updates cached version if needed
		System.out.println("\n1 ");
		request = readRequest(client);
		if (request == null) {
			System.out.println("Got from cache");
			return; // if cached version used or updated, it returns and stops
		}
		// otherwise it sends the request to the server
		else {

			// send request to peer
			Socket peer = null;
			String hashMapIP;
			int hashMapPort;
			for (Entry<String, Integer> entry : hm.entrySet()) {
				hashMapIP = entry.getKey();
				hashMapPort = entry.getValue();
				peer = sendRequestToPeer(request, hashMapIP, hashMapPort);
				response = getResponseFromPeer(peer);
			}

			if (response == null) {
				// Sends Request to Server
				System.out.println("\n2 ");
				server = sendRequestToServer(request);

				// Get Response From Server and Forward to Client
				try {
					System.out.println("\n3 ");
					response = getResponseFromServer(server);
					System.out.println("Response: " + response.toString()
							+ "\n");

					System.out.println("\n4 ");
					sendResponseToClient(response, client);

					System.out.println("\n5 ");
					addRequestResponseToCache(request, response);

					client.close();
					server.close();

				} catch (IOException e) {
					return;
				}
			}
		}
	}

	private void handlePeer(Socket peer) {

		HttpRequest request = null;

		/**
		 * Process request. If there are any exceptions, then simply return and
		 * end this request. This unfortunately means the client will hang for a
		 * while, until it timeouts.
		 */

		// Read request
		// Uses cached version if detected
		// Updates cached version if needed
		System.out.println("\n1 ");
		request = readRequestFromPeer(peer);
		if (request == null) {
			System.out.println("Got from cache, sent to peer");
			return; // send cache to peer
		} else {
			DataOutputStream toPeer;
			try {
				toPeer = new DataOutputStream(peer.getOutputStream());

				toPeer.write("false".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static HttpRequest readRequestFromPeer(Socket peer) {
		try {
			BufferedReader fromPeer = new BufferedReader(new InputStreamReader(
					peer.getInputStream()));
			/** Fill in */

			System.out.println("Reading request from peer...");
			HttpRequest request = new HttpRequest(fromPeer);
			/** Fill in */
			System.out.println("Got request from Peer.\n");

			// Check if request is in Cache
			HttpResponse cache_response = getCachedResponse(request);

			if (cache_response != null) {
				System.out.println("Request is in cache, sending to peer.");
				// send the cached version to the peerì
				sendResponseToPeer(cache_response, peer);
				peer.close();
				return null;
			}
			return request;
		} catch (IOException e) {
			System.out.println("Error reading request from client: " + e);
			return null;
		}
	}

	/** Read request */
	private static HttpRequest readRequest(Socket client) {
		try {
			BufferedReader fromClient = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			/** Fill in */

			System.out.println("Reading request...");
			HttpRequest request = new HttpRequest(fromClient);
			/** Fill in */
			System.out.println("Got request.\n");

			// Check if request is in Cache
			HttpResponse cache_response = null;
			if ((cache_response = getCachedResponse(request)) != null) {
				System.out.println("Request is in cache.");
				String cache_etag = cache_response.etag;
				System.out.println("Cache ETag: " + cache_etag + "\n\n");

				// Sending conditional GET to server
				// This shall check if cached object is outdated
				System.out.println("Sending If-None-Match request...");
				String con_get = "GET " + request.getURL() + " "
						+ request.version + CRLF + "Host: " + request.getHost()
						+ CRLF + "If-None-Match: " + cache_etag + CRLF + CRLF;

				System.out.println("Cond. GET REQUEST - \n" + con_get + "\n");

				HttpRequest r = new HttpRequest(
						convertStringToBufferedReader(con_get));
				Socket server = sendRequestToServer(r);
				HttpResponse con_get_response = getResponseFromServer(server);
				System.out.println("Done sending If-None-Match request.\n");

				System.out.println("Cond. GET RESPONSE - \n"
						+ con_get_response.toString() + "\n");

				if (con_get_response.status == 304) {
					// if the server did not return an updated version
					// send the cached version to the client
					System.out.println("Server has no updated version.");
					System.out.println("Using cached version as response.");
					sendResponseToClient(cache_response, client);
					client.close();
					server.close();
					return null;

				} else if (con_get_response.status == 200) {
					// if the server returned a packet with an etag then it
					// means it returned an updated version
					// send updated version to client
					System.out.println("Server has an updated version.");
					System.out.println("Using updated version.");
					sendResponseToClient(con_get_response, client);
					System.out.println("Updating cache with server version.");
					addRequestResponseToCache(request, con_get_response);
					client.close();
					server.close();
					return null;
				}
				client.close();
				server.close();
				return null;
			}
			return request;
		} catch (IOException e) {
			System.out.println("Error reading request from client: " + e);
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
			/** Fill in */
			DataOutputStream toServer = new DataOutputStream(
					server.getOutputStream());
			/** Fill in */
			toServer.writeBytes(request.toString());
			/** Fill in */
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
			peer = new Socket(ip, port);
			/** Fill in */
			DataOutputStream toServer = new DataOutputStream(
					peer.getOutputStream());
			/** Fill in */
			toServer.writeBytes(request.toString());
			/** Fill in */
			System.out.println("Finished sending request to peer.\n");
			return peer;

		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + request.getHost());
			System.out.println(e);
			return null;

		} catch (IOException e) {
			System.out.println("Error writing request to server: " + e);
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

//		for (Entry<String, Integer> entry : hm.entrySet()) {

			try {
//				Socket s = new Socket(entry.getKey(), entry.getValue());
//				DataOutputStream toPeer = new DataOutputStream(s.getOutputStream());
				DataInputStream fromPeer = new DataInputStream(
						server.getInputStream());
				/** Fill in */
				
				HttpResponse response = new HttpResponse(fromPeer);
				/** Fill in */

				byte[] receiveData = new byte[1024];
				fromPeer.read(receiveData);
				
				System.out.println(receiveData); // check=============
				//boolean receivedFromPeer = true;
				//String peerRec = "";
				//for (Integer i = 0; i < 5; i++) {
					//peerRec += (char) receiveData[i];
				//}
//				if (peerRec.contains("false")) {
	//				continue;
//				}

			} catch (IOException e) {
				System.out.println("Error writing response to client: " + e);
				return null;

			}
//		}
		return null;

	}

	private static void sendResponseToClient(HttpResponse response,
			Socket client) {
		try {
			System.out.println("Sending Response to Client");
			DataOutputStream toClient = new DataOutputStream(
					client.getOutputStream());
			/** Fill in */

			/** Write response to client. First headers, then body */
			toClient.writeBytes(response.toString());
			/** Fill in */
			toClient.write(response.body);
			System.out.println("Done sending Response to Client");

		} catch (IOException e) {
			System.out.println("Error writing response to client: " + e);

		}
	}

	private static void sendResponseToPeer(HttpResponse response, Socket peer) {
		try {
			System.out.println("Sending Response to Peer");
			DataOutputStream toPeer = new DataOutputStream(
					peer.getOutputStream());
			/** Fill in */

			/** Write response to client. First headers, then body */
			toPeer.writeBytes(response.toString());
			/** Fill in */
			toPeer.write(response.body);
			
			//toPeer.writeBytes(response.finalstring());
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
		cache.put(request, response);
		System.out.println("Added request and response to cache.");
	}

	/** Checks to see if inputted response is in memory cache via request */
	/** returns true or false */
	private static HttpResponse getCachedResponse(HttpRequest request) {
		try {
			System.out.println("Checking if request is in memory cache...");

			boolean answer = false;
			HttpResponse response = null;

			// check Hash Map for a matching request
			Iterator iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();
				System.out.println("Sifting through HashMap...");
				HttpRequest r = (HttpRequest) mEntry.getKey();

				System.out.println("Cache URL: " + r.getURL());
				System.out.println("Request URL: " + request.getURL());
				if (request.getURL().equals(r.getURL())) {
					System.out.println("IN CACHE!!!!! ");
					response = (HttpResponse) mEntry.getValue();
				}
			}

			System.out.println("Finished checking request.\n");

			return response;

		} catch (Exception e) {
			System.out.println("Error reading request from client: " + e);
			return null;

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
				System.out.println("Acessing HashMap of ProxyCache");
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
		new ProxycacheError3().callingThread();
		System.out.println("In main");
	}
}