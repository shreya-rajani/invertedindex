import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Directory Traverser using multi-threading
 * 
 * @author shreyarajani
 * 
 */
public class MultithreadedIndexBuilder extends DirectoryTraverser {

	/**
	 * Declaring variables
	 */
	//private static final Logger logger = LogManager.getLogger();
	private final WorkQueue workers;
	private int pending;

	/**
	 * 
	 * Initializing variables in the constructor
	 * 
	 * @param multiInvertIndex
	 *            - Object of MultithreadedInvertedIndex
	 * @param workers
	 *            - Object of WorkQueue
	 */
	public MultithreadedIndexBuilder(
			MultithreadedInvertedIndex multiInvertIndex, WorkQueue workers) {
		super(multiInvertIndex);
		this.workers = new WorkQueue();
		pending = 0;
	}

	/**
	 * Checks if the given path is a directory or not if it is a directory then
	 * it creates the worker thread to traverse the directory if it not a
	 * directory and is a file, it will add to TreeSet
	 */
	public void traverse(Path path) {
		try {
			for (Path file : Files.newDirectoryStream(path)) {
				if (file.toString().toLowerCase().endsWith(".txt")) {
					workers.execute(new DirectoryWorker(file));
				} else if (Files.isDirectory(path))
					traverse(file);
			}
		} catch (IOException e) {
			//System.out.println("Unable to traverse :" + path);
		}
	}

	/**
	 * Inner class to create the worker threads
	 * 
	 * @author shreyarajani
	 * 
	 */
	private class DirectoryWorker implements Runnable {
		private Path path;

		/**
		 * 
		 * Checks if the given path is a directory or not if it is a directory
		 * then it creates the worker thread to traverse the directory if it not
		 * a directory and is a file, it will add to TreeSet
		 * 
		 * @param path
		 *            - path sent to threads
		 */
		public DirectoryWorker(Path path) {
			//logger.debug("Worker Thread created for {}", path);
			this.path = path;
			incrementPending();
		}

		/**
		 * Overriding run() and the worker threads, adds the files to the
		 * temporary TreeSet
		 */
		@Override
		public void run() {

			InvertedIndex local = new InvertedIndex();
			InvertedIndexBuilder invertedIndexBuilder = new InvertedIndexBuilder(
					local);
			invertedIndexBuilder.parseFile(path);
			invertedIndex.addAll(local);
			decrementPending();
			//logger.debug("Worker Thread finished {}", path);
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
	public void shutdown() {
		//logger.debug("Shutting down now");
		finish();
		workers.shutdown();
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
}