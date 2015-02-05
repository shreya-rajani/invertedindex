import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to handle search results
 * 
 */
@SuppressWarnings("serial")
public class SearchServlet extends LoginBaseServlet {

	/**
	 * Method to handle GET requests
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String user = getUsername(request);

		if (user != null) {
			prepareResponse("Search Results", response);
			try {
				PrintWriter writer = response.getWriter();

				writer.println("<p>Hello " + user + "!</p>");
				writer.println("<p style=\"text-align:center;\">| <a href=\"/search_history\">View Search History</a>"
						+ " | <a href=\"/settings\">Settings</a> | <a href=\"/login?logout\">logout</a></p>");

				writer.println("<center>");

				writer.println("<h1>Search Engine</h1>");

				writer.println("<form action=\"/welcome\" method=\"post\">");

				writer.println("<p>Search for: <input type=\"text\" name=\"searchquery\" size=\"30\"></p>");
				writer.println("\t\t<input type=\"checkbox\" name=\"searchHistory\" checked=\"yes\"> Check this box to switch on Search History");
				writer.println("<p><input type=\"submit\" value=\"Search\"></p>");

				writer.println("</form>");

				writer.println("</center>");

				String searchOutput = writer.toString();
				LoginBaseServlet.sindex.printSearchToHtml(searchOutput, writer);

				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			finishResponse(response);
		}
	}

}
