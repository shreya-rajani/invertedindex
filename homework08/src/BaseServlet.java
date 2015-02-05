import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * An {@link HttpServlet} with several additional utility methods. Used by
 * several servlet examples.
 */
@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

	/** Log used by Jetty (not log4j2). */
	public static Logger log = Log.getLogger(BaseServlet.class);

	/**
	 * Returns the current date and time in a long format.
	 * 
	 */
	public String getShortDate() {
		String format = "hh:mm a 'on' EEE, MMM dd, yyyy";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * Prepares the HTTP response by setting the content type and adding header
	 * HTML code.
	 * 
	 */
	public void prepareResponse(String title, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();

			out.printf("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n\n");
			out.printf("<html>\n\n");
			out.printf("<head>\n");
			out.printf("\t<title>%s</title>\n", title);
			out.printf("\t<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\n");
			out.printf("</head>\n\n");
			out.printf("<body>\n\n");
		} catch (IOException ex) {
			log.warn("Unable to prepare HTTP response.");
			return;
		}
	}

	/**
	 * Finishes the HTTP response by adding footer HTML code and setting the
	 * response code.
	 * 
	 */
	public void finishResponse(HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();

			out.printf("\n");
			out.printf("<p style=\"font-size: 10pt; font-style: italic;\">");
			out.printf("Last updated at %s.", getShortDate());
			out.printf("</p>\n\n");

			out.printf("</body>\n");
			out.printf("</html>\n");

			out.flush();

			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException ex) {
			log.warn("Unable to finish HTTP response.");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}

	/**
	 * Gets the cookies form the HTTP request, and maps the cookie key to the
	 * cookie value.
	 * 
	 */
	public Map<String, String> getCookieMap(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}
		return map;
	}

	/**
	 * Clears all of the cookies included in the HTTP request.
	 * 
	 */
	public void clearCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
