import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Directory Traverser using multi-threading
 * @author shreyarajani
 *
 */

public class MultithreadedDirectoryTraverser {

	/**
	 * Declaring variables
	 */
	private static final Logger logger = LogManager.getLogger();
	private final WorkQueue workers;
	private final TreeSet<Path> paths;
	private final MultiReaderLock lock;
	private int pending;

	/**
	 * Initializing variables in the constructor
	 */
	public MultithreadedDirectoryTraverser() {
		workers = new WorkQueue();
		lock = new MultiReaderLock();
		paths = new TreeSet<Path>();
		pending = 0;
	}

	/**
	 * Checks if the given path is a directory or not
	 * if it is a directory then it creates the worker thread to traverse the directory
	 * if it not a directory and is a file, it will add to TreeSet
	 */
	void traverseDirectory(Path dir, String ext) {
		String ext1 = dir.toString();
		if (Files.isDirectory(dir)) {
			workers.execute(new DirectoryWorker(dir, ext));
		} else if (Files.exists(dir)) {
			if (ext1.endsWith(ext)) {
				lock.lockWrite();
				paths.add(dir);
				lock.unlockWrite();
			}
		}
	}

	/**
	 * Inner class to create the worker threads
	 * @author shreyarajani
	 *
	 */
	private class DirectoryWorker implements Runnable {
		private Path directory;
		private TreeSet<Path> tempPath;
		private String ext;

		/**
		 * The worker threads adds the files in the particular directory to a temporary TreeSet
		 * @param directory
		 * @param ext
		 */
		public DirectoryWorker(Path directory, String ext) {
			tempPath = new TreeSet<Path>();
			logger.debug("Worker Thread created for {}", directory);
			this.directory = directory;
			this.ext = ext;
			incrementPending();
		}

		/**
		 * Overriding run() and the worker threads, adds the files to the temporary TreeSet
		 */
		@Override
		public void run() {

			try {
				for (Path path : Files.newDirectoryStream(directory)) {
					if (Files.isDirectory(path)) {
						workers.execute(new DirectoryWorker(path, ext));
					} else if (path.toString().endsWith(ext)) {
						tempPath.add(path);
					}
				}

				lock.lockWrite();
				paths.addAll(tempPath);
				lock.unlockWrite();

				decrementPending();

			} catch (IOException e) {
				logger.warn("Unable to parse {}", directory);
				logger.catching(Level.DEBUG, e);
			}

			logger.debug("Worker Thread finished {}", directory);
		}
	}

	/**
	 * finish method
	 */
	public synchronized void finish() {
		try {
			while (pending > 0) {
				logger.debug("Waiting...");
				this.wait();
			}
		} catch (InterruptedException e) {
			logger.debug("Finish interrupted", e);
		}
	}

	/**
	 * reset path method
	 */
	public synchronized void reset() {
		finish();
		lock.lockWrite();
		paths.clear();
		lock.unlockWrite();
		logger.debug("Counters reset");
	}

	/**
	 * shutdown method
	 */
	public synchronized void shutdown() {
		logger.debug("Shutting down now");
		finish();
		workers.shutdown();
	}

	/**
	 * getPaths methods
	 * @return
	 */
	public Set<Path> getPaths() {
		finish();
		Set<Path> safePath = Collections.unmodifiableSet(paths);
		return safePath;
	}

	/**
	 * increment pending method
	 */
	private synchronized void incrementPending() {
		pending++;
		logger.debug("Pending is now {}", pending);
	}

	/**
	 * decrement pending method
	 */
	private synchronized void decrementPending() {
		pending--;
		logger.debug("Pending is now {}", pending);

		if (pending <= 0) {
			this.notifyAll();
		}
	}

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		MultithreadedDirectoryTraverser demo = new MultithreadedDirectoryTraverser();

		demo.traverseDirectory(
				Paths.get("/Users/shreyarajani/MS-Sem1/SD/prog/project1/input/index"),
				".txt");
		System.out.println("Paths of the files are : ");

		for (Path path : demo.getPaths()) {
			System.out.println(path.toString());
		}
		demo.shutdown();
	}
}