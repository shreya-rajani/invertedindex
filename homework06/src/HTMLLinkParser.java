import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For this homework assignment, you must create a regular expression that is
 * able to parse links from HTML. Your code may assume the HTML is valid, and
 * all attributes are properly quoted and URL encoded.
 * 
 * <p>
 * See the following link for details on the HTML Anchor tag: <a
 * href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a">
 * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a </a>
 * 
 * @author TODO: FILL THIS IN
 * @see HTMLLinkTester
 */
public class HTMLLinkParser {

	/**
	 * The regular expression used to parse the HTML for links.
	 * 
	 * TODO: FILL THIS IN
	 */

	// private static final String pregex = "^([a-zA-Z][a-zA-Z0-9+.-]*)://";
	// private static final String dregex = "([^/?#]+)";
	// private static final String rregex = "(/[^?#]*)?";
	// private static final String qregex = "(?:\\?([^#]*))?";
	// private static final String fregex = "(?:#(.*))?$";
	// working regular expression
	// private static final String REGEX = pregex + dregex + rregex + qregex +
	// fregex;

	public static final String s1 = "(?<=\")"; //one or no < or =
	public static final String s2 = "([^\\s)"; // skip space
	public static final String s3 = "(!\\%\\)"; // not followed by %
	public static final String s4 = "(^&\\()\")"; //not & or () 
	public static final String s5 = "(?]+\\.\\S+)"; //one or no }  one or more + S no space 
	public static final String s6 = "(?=\")"; //one or no =

	public static final String REGEX = s1 + s2 + s3 + s4 + s5 + s6;

	/**
	 * The group in the regular expression that captures the raw link. This will
	 * usually be 1, depending on your specific regex.
	 * 
	 * TODO: FILL THIS IN
	 */
	public static final int GROUP = 1;

	/**
	 * Parses the provided text for HTML links. You should not need to modify
	 * this method.
	 * 
	 * @param text
	 *            - valid HTML code, with quoted attributes and URL encoded
	 *            links
	 * @return list of links found in HTML code
	 */
	public static ArrayList<String> listLinks(String text) {
		// list to store links
		ArrayList<String> links = new ArrayList<String>();

		// compile string into regular expression
		Pattern p = Pattern.compile(REGEX);

		// match provided text against regular expression
		Matcher m = p.matcher(text);

		// loop through every match found in text
		while (m.find()) {
			// add the appropriate group from regular expression to list
			links.add(m.group(GROUP));
		}

		return links;
	}
}