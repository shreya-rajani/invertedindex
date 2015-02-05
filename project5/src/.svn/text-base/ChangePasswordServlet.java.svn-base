import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ChangePasswordServlet extends LoginBaseServlet {

	protected static final LoginDatabaseHandler dbhandler = LoginDatabaseHandler
			.getInstance();

	/**
	 * Method to handle GET requests
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String user = getUsername(request);
		if (user != null) {
			prepareResponse("Change Password", response);
			try {
				PrintWriter out = response.getWriter();
				prepareResponse("Change Password", response);
				out.println("<p style=\"text-align:center;\">" + "| <a href=\"/welcome\">Search</a> | <a href=\"/search_history\">View Search History</a>"
						+ " | <a href=\"/settings\">Settings</a> | <a href=\"/login?logout\">logout</a></p>");

				out.println("<form method=\"post\">");
				out.println("<table border=\"0\">");
				out.println("\t<tr>");
				out.println("\t\t<td>New Password: </td>");
				out.println("\t\t<td><input type=\"password\" name=\"newpassword\" size=\"30\"></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<p><input type=\"submit\" value=\"Change Password\"></p>");
				//out.println("<p><input type=\"submit\" value=\"Delete Account\"></p>");
				out.println("</form>");
				finishResponse(response);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to handle POST requests
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		String user = getUsername(request);
		String newpassword = request.getParameter("newpassword");

		try {
			dbhandler.updateUser(user, newpassword);

			response.sendRedirect("/login?new_password");
		} catch (Exception e) {
		}
	}
}
