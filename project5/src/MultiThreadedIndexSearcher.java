import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class parses the file and prints the partially searched inverted index
 * 
 * @author shreyarajani
 * 
 */
public class MultiThreadedIndexSearcher extends IndexSearcher {

	//private static final Logger logger = LogManager.getLogger();
	private final MultiReaderLock queryLock;
	private int pending;
	private final WorkQueue workers;
	private final MultithreadedInvertedIndex index;
	
	/**
	 * Constructor to initialize the values of queryList and inverted index
	 * object
	 * 
	 * @param index - object of MultithreadedInvertedIndex
	 */
	public MultiThreadedIndexSearcher(MultithreadedInvertedIndex index,
			WorkQueue workers) {
		super(index);
	this.index = index;
		

		queryLock = new MultiReaderLock();
		pending = 0;
		this.workers = workers;
	}

	/**
	 * /** This method parses the query file
	 * 
	 * @param queryFile
	 *            - the file which contains the query
	 * 
	 * @param someIndex
	 *            - object of multithreadedinvertedindex
	 */

	@Override
	public void parseQuery(Path queryFile) {

		try (BufferedReader br = Files.newBufferedReader(queryFile,
				Charset.forName("UTF-8"));) {

			String queryline = null;
			while ((queryline = br.readLine()) != null) {
				word(queryline);
			}

		} catch (IOException ioe) {
			//System.out.println("Unable to parse the file : " + queryFile);
		}
	}
	
	public void word(String queryline){
		
		if (queryline.trim().length() != 0) {
			queryline = queryline.trim().toLowerCase()
					.replaceAll("_", "");
			addSearchResults(queryline, new ArrayList<Result>());
			workers.execute(new ParseQueryWorker(index, queryline));
		}
		
	}
	
	

	/**
	 * Multi-threads the addSearchResults
	 * 
	 */
	@Override
	public void addSearchResults(String line, List<Result> otherSearchResults) {
		queryLock.lockWrite();
		super.addSearchResults(line, otherSearchResults);
		queryLock.unlockWrite();
	}

	@Override
	public void printSearchIndex(String resultFile){
		queryLock.lockWrite();
		super.printSearchIndex(resultFile);
		queryLock.unlockWrite();
	}
	
	
	/**
	 * Inner class that multi-threads the ParseQuery
	 * 
	 * @author shreyarajani
	 * 
	 */
	public class ParseQueryWorker implements Runnable {
		MultithreadedInvertedIndex someIndex;
		String ln;

		ParseQueryWorker(MultithreadedInvertedIndex someIndex, String line) {
			this.ln = line;
			this.someIndex = someIndex;
			incrementPending();
		}

		public void run() {
			addSearchResults(ln, someIndex.partialSearch(ln));
			decrementPending();
		}
	}

	/**
	 * increment pending method
	 */
	private synchronized void incrementPending() {
		pending++;
		//logger.debug("Pending is now {}", pending);
	}

	/**
	 * decrement pending method
	 */
	private synchronized void decrementPending() {
		pending--;
		//logger.debug("Pending is now {}", pending);

		if (pending <= 0) {
			this.notifyAll();
		}
	}

	/**
	 * finish method
	 */
	public synchronized void finish() {
		try {
			while (pending > 0) {
				//logger.debug("Waiting...");
				this.wait();
			}
		} catch (InterruptedException e) {
			//logger.debug("Finish interrupted", e);
		}
	}

	/**
	 * shutdown method
	 */
	public synchronized void shutdown() {
		//logger.debug("Shutting down now");
		finish();
		workers.shutdown();
	}
}