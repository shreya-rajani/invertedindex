import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class LoginServer {
	//private static int PORT = 8080;
	protected static InvertedIndex ii;
	

	public static void servermain(InvertedIndex index, int port) {
		Server server = new Server(port);
		ii = index;
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(LoginUserServlet.class,    "/login");
		handler.addServletWithMapping(LoginRegisterServlet.class, "/register");
		handler.addServletWithMapping(LoginWelcomeServlet.class,  "/welcome");
		handler.addServletWithMapping(SearchServlet.class,  "/searchresults");
		handler.addServletWithMapping(HistoryServlet.class,  "/search_history");
		handler.addServletWithMapping(ChangePasswordServlet.class,  "/settings");
	    handler.addServletWithMapping(LoginRedirectServlet.class, "/*");
	    server.setHandler(handler);
		server.setHandler(handler);
		
		try {
			server.start();
			server.join();

		}
		catch (Exception ex) {
			System.out.println("Interrupted while running server." + ex.getMessage());
			System.exit(-1);
		}
	}
}