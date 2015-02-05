import java.io.File;
import java.io.IOException;

public class Driver {

	/**
	 * Constructor for the class Driver
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 */
	public Driver(String[] args) {

		try {
			InvertedIndex ii = new InvertedIndex();
			ArgumentParser aParser = new ArgumentParser(args);
			String directory = aParser.getValue("-d");

			if (aParser.hasFlag("-d") == false) {
				System.out.println("Improper input... Exiting...");
				System.exit(0);
			}

			File dir = new File(directory);
			if (!dir.exists()) {
				System.out.println("Directory does not exist...\nExiting...");
				System.exit(0);
			}

			String qFilePath = aParser.getValue("-q");
			if (aParser.hasFlag("-q") == false) {
				System.out.println("No query file specified...\nExiting...");
				System.exit(0);
			}

			File qFile = new File(qFilePath);
			if (!qFile.exists()) {
				System.out.println("Query file does not exist...\nExiting...");
				System.exit(0);
			}

			boolean iFlag = aParser.hasFlag("-i");

			int numThreads = 0;
			try {
				numThreads = Integer.parseInt(aParser.getValue("-t"));
			} catch (Exception ex) {
				System.out
						.println("Number of threads are not specified, executing with 5 threads...");
				numThreads = 5;
			}

			TraverseDirectory td = new TraverseDirectory(numThreads);
			try {
				td.dirTraverse(dir, ii);
			} catch (IOException e1) {
			}
			td.shutdown();

			FileParser fp = new FileParser(numThreads);
			fp.parseQueryFile(ii, qFilePath);
			fp.shutdown();

			if (iFlag == true) {
				try {
					ii.writeInvertedIndex();
				} catch (IOException e) {
				}
			}

			try {
				ii.writeSearchResults();
			} catch (IOException e) {
			}
		}

		finally {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new Driver(args);
	}

}