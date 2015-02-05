/**
 * ProxyCache.java - Simple caching proxy
 *
 * $Id: ProxyCache.java,v 1.3 2004/02/16 15:22:00 kangasha Exp $
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {

	/** Port for the proxy */
	private static int port;

	/** Socket for client connections */
	private static ServerSocket socket;

	private static String CRLF = "\r\n";
	private static HashMap<HttpRequest, HttpResponse> tree;

	/** Create the ProxyCache object and the socket */
	public static void init(int p) {
		port = p;
		try {
			socket = new ServerSocket(port);
			tree = new HashMap<HttpRequest, HttpResponse>();

		} catch (IOException e) {
			System.out.println("Error creating socket: " + e);
			System.exit(-1);

		}
	}

	public static void handle(Socket client) {
		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;

		/*
		 * Process request. If there are any exceptions, then simply return and
		 * end this request. This unfortunately means the client will hang for a
		 * while, until it timeouts.
		 */

		request = rr(client);
		if (request == null)
			return;

		server = toServer(request);

		try {
			response = fromServer(server);

			toClient(response, client);
			tree.put(request, response);

			client.close();
			server.close();

		} catch (IOException e) {
			return;
		}

	}

	private static HttpRequest rr(Socket client) {
		try {
			BufferedReader fromClient = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			/** Fill in */

			HttpRequest request = new HttpRequest(fromClient);
			/** Fill in */
			HttpResponse cres = null;
			if ((cres = savedResponse(request)) != null) {

				String get = "GET " + request.getURI() + " " + request.version
						+ CRLF + "Host: " + request.getHost() + CRLF
						+ "If-None-Match: " + cres.v + CRLF + CRLF;

				HttpRequest r = new HttpRequest(convert(get));
				Socket server = toServer(r);

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

	private static HttpResponse savedResponse(HttpRequest request) {
		try {
			HttpResponse response = null;

			// check Hash Map for a matching request
			Iterator iter = tree.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();
				HttpRequest r = (HttpRequest) mEntry.getKey();

				if (request.getURI().equals(r.getURI())) {
					response = (HttpResponse) mEntry.getValue();
				}
			}
			return response;

		} catch (Exception e) {
			System.out.println("Error reading request from client: " + e);
			return null;

		}
	}

	private static Socket toServer(HttpRequest request) {
		try {
			Socket server = null;
			server = new Socket(request.getHost(), request.getPort());
			/** Fill in */
			DataOutputStream toServer = new DataOutputStream(
					server.getOutputStream());
			/** Fill in */
			toServer.writeBytes(request.toString());
			/** Fill in */
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
	private static HttpResponse fromServer(Socket server) {
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

	private static void toClient(HttpResponse response, Socket client) {
		try {
			DataOutputStream toClient = new DataOutputStream(
					client.getOutputStream());
			/** Fill in */

			toClient.writeBytes(response.toString());
			/** Fill in */
			toClient.write(response.body);
			System.out.println("Done");

		} catch (IOException e) {
			System.out.println("Error writing response to client: " + e);
		}
	}

	private static BufferedReader convert(String str) {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(str.getBytes())));
		return br;
	}

	/** Read command line arguments and start proxy */
	public static void main(String args[]) {
		int myPort = 0;
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
		System.out.println("Ready for request");

		/**
		 * Main loop. Listen for incoming connections and spawn a new thread for
		 * handling them
		 */
		Socket client = null;
		while (true) {
			try {
				client = socket.accept();
				/** Fill in */
				handle(client);

			} catch (IOException e) {
				System.out.println("Error reading request from client: " + e);
				/*
				 * Definitely cannot continue processing this request, so skip
				 * to next iteration of while loop.
				 */
				continue;
			}
		}
	}
}