import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class crawls each webpage, and creates a new thread for each link in the
 * webpage.
 * 
 * @author shreyarajani
 * 
 */
public class WebCrawler {

	// private static final Logger logger = LogManager.getLogger();
	private String urlFound;
	private HashSet<String> linksSet;
	private MultiReaderLock lock;
	private Integer pending;
	private MultithreadedInvertedIndex map;
	private WorkQueue workers;

	public static final String regex = "<[aA]\\s+.*?[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+?)\"";

	/**
	 * Constructor that initializes the values
	 * 
	 * @param map
	 *            - MultithreadedInvertedIndex map
	 * @param workers
	 *            - Work Queue qorkers
	 */
	public WebCrawler(MultithreadedInvertedIndex map, WorkQueue workers) {
		linksSet = new HashSet<String>();
		lock = new MultiReaderLock();

		this.map = map;
		this.workers = workers;
		pending = 0;

	}

	/**
	 * Parses line
	 * 
	 * @param line
	 *            - the line to be parses
	 * @param tempIndex
	 *            - object of InvertedIndex
	 * @param url
	 *            - URL to add in the InvertedIndex
	 */
	public void parser(String line, InvertedIndex tempIndex, String url) {
		int ctr = 0;
		if (!line.trim().equals("")) {
			line = line.trim();
			String[] words = line.split(" ");
			for (String word : words) {
				word = word.replaceAll("[^a-zA-Z0-9\\s]", "").trim()
						.toLowerCase();

				if (!word.equals("")) {
					ctr = ctr + 1;
					tempIndex.addEntry(word, url, ctr);
				}
			}
		}
	}

	/**
	 * To remove the extra data except the string
	 * 
	 * @param html
	 *            - the html to be cleaned
	 * @return text
	 */
	public String cleanHTML(StringBuffer html) {
		String text = html.toString();
		text = stripElement("script", text);
		text = stripElement("style", text);
		text = stripTags(text);
		text = stripEntities(text);
		return text;
	}

	/**
	 * Gets the html page
	 * 
	 * @param link
	 *            - to be fetched
	 * @param tempii
	 *            - InvertedIndex
	 * @return
	 */
	public LinkedHashSet<String> htmlFetcher(URL link, InvertedIndex tempii) {
		LinkedHashSet<String> tempLinks = new LinkedHashSet<String>();

		Socket socket;
		try {
			socket = new Socket(link.getHost(), 80);

			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String request = "GET " + link.getFile() + " HTTP/1.1"
					+ "\nHost : " + link.getHost() + "\n" + "Connection: close"
					+ "\r\n\n";
			writer.print(request);
			writer.flush();

			String ignoreHeader = new String();
			boolean header = true;
			boolean htmlContent = false;
			while (header) {
				ignoreHeader = reader.readLine();
				if (ignoreHeader.contains("Content-Type: text/html")) {
					htmlContent = true;
				}

				if (ignoreHeader.isEmpty()) {
					header = false;
				}
			}

			if (htmlContent) {
				String input;
				StringBuffer htmlFetch = new StringBuffer();
				boolean htmlStart = false;
				while (!(input = reader.readLine()).toLowerCase().startsWith(
						"</html>")) {
					if (input.toLowerCase().startsWith("<html")) {
						input = reader.readLine();
						htmlStart = true;
					}
					if (htmlStart)
						htmlFetch.append(" " + input);
				}

				socket.close();
				reader.close();
				writer.close();
				if (linksSet.size() < 50)
					tempLinks = linkFinder(htmlFetch);
				String cleaned = cleanHTML(htmlFetch);
				parser(cleaned, tempii, link.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempLinks;

	}

	/*
	 * Strips the elements
	 */
	public String stripElement(String name, String html) {
		String pattern = "(?is)<" + name + "(.*?)>" + "(.*?)/" + name + "[ ]*>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll("");
		return result;
	}

	/**
	 * Strips the tags
	 * 
	 * @param html
	 *            - html from which the tags are to be removed
	 * @return the text free from tags
	 */
	public String stripTags(String html) {
		String pattern = "(?is)<.*?>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String finalText = m.replaceAll(" ");
		return finalText;
	}

	public String stripEntities(String html) {
		String pattern = "(?is)&(.*?);";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll(" ");
		return result;

	}

	/**
	 * to find the links
	 * 
	 * @param text
	 *            to match the text
	 * @return link
	 */
	private LinkedHashSet<String> linkFinder(StringBuffer text) {
		LinkedHashSet<String> link = new LinkedHashSet<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(text);

		while (match.find()) {
			if (match.group(1).startsWith("#")) {
				continue;
			} else if (!link.contains(match.group(1))) {
				link.add(match.group(1));
			}
		}
		return link;
	}

	/**
	 * This method parses the words and calls the creates a pool of threads
	 * 
	 * @param url
	 *            url to parse the words
	 */
	public void parseWords(String url) {
		String pattern = "(?is)((.*)/)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(url);
		if (m.find())
			urlFound = m.group(1);
		linksSet.add(url);
		// myCleaner = new HTMLCleaner(foundLinks);
		try {
			workers.execute(new final1(new URL(url)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Combines the links in the set
	 * 
	 * @param tempLinks
	 *            - to store links
	 */
	public void combine(LinkedHashSet<String> tempLinks)
			throws MalformedURLException {

		for (String url : tempLinks) {
			URL temp;
			if (url.startsWith("http")) {
				temp = new URL(url);
			} else {
				temp = new URL(new URL(urlFound), url);
			}
			lock.lockWrite();

			if (!linksSet.contains(temp.toString()) && linksSet.size() < 50) {
				linksSet.add(temp.toString());
				workers.execute(new final1(temp));
			}
			lock.unlockWrite();
		}

	}

	/**
	 * Class the assigns work to workers
	 * 
	 * @author shreyarajani
	 * 
	 */
	private class final1 implements Runnable {

		private URL link;
		private LinkedHashSet<String> tempList;

		public final1(URL link) {
			this.link = link;
			incrementPending();

		}

		/**
		 * run method
		 */
		@Override
		public void run() {
			InvertedIndex tempii = new InvertedIndex();
			tempList = htmlFetcher(link, tempii);
			map.addAll(tempii);
			try {
				combine(tempList);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			decrementPending();
		}
	}

	public synchronized void finish() {
		try {
			while (pending > 0) {
				// logger.debug("Waiting until finished");
				this.wait();
			}
		} catch (InterruptedException e) {
			// logger.debug("Finish interrupted", e);
		}
	}

	public synchronized void reset() {
		finish();
		// logger.debug("Counters reset");
	}

	public synchronized void shutdown() {
		// logger.debug("Shutting down");
		finish();
		workers.shutdown();
	}

	private synchronized void incrementPending() {
		pending++;
		// logger.debug("Pending is now {}", pending);
	}

	private synchronized void decrementPending() {
		pending--;
		// logger.debug("Pending is now {}", pending);
		if (pending <= 0) {
			this.notifyAll();
		}
	}
}
