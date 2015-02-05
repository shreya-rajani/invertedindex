import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginWelcomeServlet extends LoginBaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);

		if (user != null) {
			prepareResponse("Welcome", response);

			PrintWriter writer = response.getWriter();

			writer.println("<p>Hello " + user + "!</p>");

			writer.println("<center>");

			writer.println("<p style=\"text-align:center;\">| <a href=\"/search_history\">View Search History</a>"
					+ " | <a href=\"/settings\">Settings</a> | <a href=\"/login?logout\">logout |</a> |</p>");

			writer.println("<h1>Search Engine</h1>");

			writer.println("<form action=\"/welcome\" method=\"post\">");

			writer.println("<p>Search for: <input type=\"text\" name=\"searchquery\" size=\"30\"></p>");

			writer.println("\t\t<input type=\"checkbox\" name=\"searchHistory\" checked=\"yes\"> Check this box to switch on Search History");
			writer.println("<p><input type=\"submit\" value=\"Search\"></p>");

			writer.println("</form>");

			writer.println("</center>");

			finishResponse(response);
		} else {
			response.sendRedirect("/login");
		}
	}

	/**
	 * doPost method
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String username = getUsername(request);
		String searchHistory = request.getParameter("searchHistory");

		try {
			String query = request.getParameter("searchquery");

			query = WebCrawler.stripTags(query);

			LoginBaseServlet.sindex.mapFinalQuery(query);

			if (searchHistory != null) {
				dbhandler.insertSearchHistory(username, query);
			}

			response.sendRedirect("/searchresults");
		} catch (Exception e) {
			// log.warn("Unable to properly write HTTP response.");
		}
	}
}