import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CookieMonsterServlet extends BaseServlet {

	public CookieMonsterServlet() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.info("MessageServlet handling GET request.");

		try {
			PrintWriter out = response.getWriter();
			prepareResponse("Cookie Monster", response);
			out.printf("<h1>Saved Cookies</h1>%n%n");

			Map<String, String> cookies = getCookieMap(request);
			if (cookies.isEmpty()) {
				out.print("<p>No saved cookies...</p>");
			} else {
				for (String username : cookies.keySet()) {
					out.printf("<p><b>%s: </b>%s</p>\n\n", username,
							cookies.get(username));
				}
			}

			out.println();
			out.printf("<h1>Edit Cookies</h1>%n%n");
			out.println("<form method=\"post\" action=\"/cookies\">\n");
			out.println("<table cellspacing=\"0\" cellpadding=\"10\"");
			out.println("<tr>");
			out.println("\t<td nowrap>Name:</td>");
			out.println("\t<td>");
			out.println("\t\t<input type=\"text\" name=\"uname\" maxlength=\"100\" size=\"60\">");
			out.println("\t</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("\t<td nowrap>Value:</td>");
			out.println("\t<td>");
			out.println("\t\t<input type=\"text\" name=\"value\" maxlength=\"100\" size=\"60\"><br>");
			out.println("\t</td>");
			out.println("\t</tr>");
			out.println("\t<tr>");
			out.println("\t<td>");
			out.println("\t\t<input type=\"checkbox\" name=\"deleteCookie\"> Delete this cookie");
			out.println("\t</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<p><input type=\"submit\" value=\"Submit\"></p>\n");
			out.println("</form>\n");

			finishResponse(response);
		} catch (IOException e) {
			log.warn("Unable to properly write HTTP response.");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		log.info("MessageServlet handling POST request.");

		try {
			String name = request.getParameter("uname");
			String value = request.getParameter("value");
			String clearCookie = request.getParameter("deleteCookie");

			value = value == null ? "" : value;
			name = name == null ? "anonymous" : name;

			if (clearCookie == null) {
				response.addCookie(new Cookie(name, value));
			} else {
				clearCookie(name, response);
			}
			response.sendRedirect("/cookies");
		} catch (Exception e) {
			log.warn("Unable to write HTTP response.");
		}
	}
}
