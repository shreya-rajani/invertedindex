import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class HistoryServlet extends LoginBaseServlet {

	/**
	 * Method to handle GET requests
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String user = getUsername(request);

		if (user != null) {
			prepareResponse("Search History", response);

			try {
				PrintWriter out = response.getWriter();

				out.println("<p style=\"text-align:center;\">| <a href=\"/search_history\">View Search History</a>"
						+ " | <a href=\"/settings\">Settings</a> | <a href=\"/login?logout\">logout</a> |</p>");

				out.println("<h4>Your recent search history:</h4>");

				dbhandler.getHistory(user, out);

				out.println("<form action=\"/search_history\" method=\"post\">");
				out.println("<p><input type=\"submit\" value=\"Clear History\"></p>");
				out.println("</form>");
			} catch (IOException ex) {
				// log.warn("Unable to write response body.", ex);
			}
			finishResponse(response);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// log.info("LoginWelcomeServlet handling POST request.");

		String user = getUsername(request);

		try {
			dbhandler.clearHistory(user);

			doGet(request, response);
		} catch (Exception e) {
			// log.warn("Unable to properly write HTTP response.");
		}
	}
}
