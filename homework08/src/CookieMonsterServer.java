import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class CookieMonsterServer {
	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

		ServletContextHandler servletContext = new ServletContextHandler();
		servletContext.setContextPath("/");
		servletContext
				.addServlet(new ServletHolder(new CookieMonsterServlet()),"/*");
		server.setHandler(servletContext);

		server.start();
		server.join();
	}
}
