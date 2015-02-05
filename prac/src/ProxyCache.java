import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {

	private static String CRLF = "\r\n";

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
			/** Fill in */
			cache = new HashMap<String, String>(); // create cache storage

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

		// HttpResponse cache_response = getCachedResponse(request);

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
			// Sends Request to Server
			System.out.println("\n2 ");
			server = sendRequestToServer(request);

			// Get Response From Server and Forward to Client
				System.out.println("\n3 ");
				response = getResponseFromServer(server);
				System.out.println("Response: " + response.toString() + "\n");

				System.out.println("\n4 ");
				sendResponseToClient(response, client);

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

	/** Read request */
	private static HttpRequest readRequest(Socket client) {
		try {
			BufferedReader fromClient = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			HttpRequest request = new HttpRequest(fromClient);// initialize the
																// request obj
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

	private static void sendResponseToClient(HttpResponse response,
			Socket client) {
		try {
			System.out.println("Sending Response to Client");
			DataOutputStream toClient = new DataOutputStream(
					client.getOutputStream());
			/** Fill in */

			/** Write response to client. First headers, then body */
			System.out.println("Writing Headers to client...");
			toClient.writeBytes(response.toString());
			/** Fill in */
			System.out.println("Writing Body to client...");
			System.out.println("Body: " + response.body);
			toClient.write(response.body);
			System.out.println("Done sending Response to Client");

		} catch (IOException e) {
			System.out.println("Error writing response to client: " + e);

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
			System.out.println("Adding response to file complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Added request and response to cache.");
	}

	private static BufferedReader convertStringToBufferedReader(String str) {
		BufferedReader b = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(str.getBytes())));
		return b;
	}

	/** -------------------------------------------------- */
	/** Read command line arguments and start proxy */
	public static void main(String args[]) {
		int myPort = 0;
		System.out.println("Setting up socket.");
		try {
			myPort = Integer.parseInt(args[0]);

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
		 * Main loop. Listen for incoming connections and spawn a new thread for
		 * handling them
		 */
		Socket client = null;
		int c = 0;
		while (true) {
			try {
				client = socket.accept();
				/** Fill in */
				System.out.println("\n-----------------------------------");
				System.out.println("- " + c + ": Got connection " + client);
				handle(client);
				System.out.println("End connection " + client + "\n\n");
				System.out.println("-----------------------------------\n");
				c++;

			} catch (IOException e) {
				System.out.println("Error reading request from client: " + e);
				/**
				 * Definitely cannot continue processing this request, so skip
				 * to next iteration of while loop.
				 */
				continue;

			}
		}

	}

}