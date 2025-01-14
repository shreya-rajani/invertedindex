import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the Driver class which takes in the argument from the console and
 * then separates the flags and the name and it calls the other functions
 * 
 * @author shreyarajani
 * 
 */
public class Driver {

	/**
	 * Calls all the flags and initiates the server and the crawler
	 * 
	 * @param args
	 *            - input from the console by the user
	 */
	public static void main(String[] args) {
		ArgumentParser argumentParser = new ArgumentParser(args);

		if (argumentParser.hasFlag("-t")) {
			int threadCount;
			try {
				threadCount = Integer.parseInt(argumentParser.getValue("-t"));
			} catch (NumberFormatException e) {
				threadCount = 5;
			}

			MultithreadedInvertedIndex index = new MultithreadedInvertedIndex();
			if (threadCount > 0) {
				WorkQueue workers = new WorkQueue(threadCount);

				if (argumentParser.hasFlag("-u")) {
					System.out.println("URL Found. ");
					String url = argumentParser.getValue("-u");
					WebCrawler webCrawler = new WebCrawler(index, workers);
					
					webCrawler.parseWords(url);
					webCrawler.finish();

					System.out.println("Hi1");
					int port = Integer.parseInt(argumentParser.getValue("-p"));
					if (!argumentParser.hasFlag("-p")) {
						System.out.println("Please specify a port.");
						System.exit(0);
					}

					System.out.println("PORT Found !!");
					LoginServer.servermain(index, port);

				}

				workers.shutdown();
			}

		} else {
			System.out.println("Invalid input.");
			System.out
					.println("Usage: -t <number of threads> -u <URL> -p <port number>");

		}
	}
}