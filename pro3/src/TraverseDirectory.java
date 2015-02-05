import java.io.File;
import java.io.IOException;

/**
 * Class to handle the directory to be parsed for files
 * and to handle multi-threading of building the inverted index 
 * 
 *
 */
public class TraverseDirectory {
	private WorkQueue workers;
	private int pending;
	
	/**
	 * Constructor for TraverseDirectory
	 * @param numThreads
	 */
	public TraverseDirectory(int numThreads) {
		workers = new WorkQueue(numThreads);
		pending = 0;
	}
	
	/**
	 * Creates a new DirectoryWorker thread for each file to be parsed in the directory
	 * @param dir Directory to be searched for files and sub-directories for parsing
	 * @param ii Instance of type InvertedIndex
	 * @throws IOException
	 */
	public void dirTraverse(File dir, InvertedIndex ii) throws IOException {

		workers.execute(new DirectoryWorker(ii, dir));
		
		while(getPending() > 0) {

			synchronized(this) {
				
				try {
					wait();
				} 
				catch (InterruptedException ex) {
					System.out.println("Interrupted while waiting...");
				}
			}
		}
	}
	
	/**
	 * Method to retrieve the pending variable
	 * @return
	 */
	private synchronized int getPending() {
		return pending;
	}
	
	/**
	 * Method to update the pending variable
	 * @param amount
	 */
	private synchronized void updatePending(int amount) {
		pending += amount;

		if(pending <= 0) {
			notifyAll();
		}
	}
	
	/**
	 * Class to create a new thread for every file present in the directory passed
	 * 
	 *
	 */
	private class DirectoryWorker implements Runnable {
		private File dir;
		private InvertedIndex ii;

		public DirectoryWorker(InvertedIndex ind, File dir) {
			this.dir = dir;
			ii = ind;

			updatePending(1);
		}

		@Override
		public void run() {

			File[] files = dir.listFiles();

			for(File inputFile: files) {
				if(inputFile.isDirectory() && inputFile.canRead()) {
					
					workers.execute(new DirectoryWorker(ii, inputFile));
				}
				else {
					if(!(inputFile.getAbsolutePath().endsWith(".txt") || inputFile.getAbsolutePath().endsWith(".TXT"))) {
						continue;
					}
					FileParser.findWordPosition(ii, inputFile);
				}
			}

			updatePending(-1);
		}
	}
	
	/**
	 * To terminate threads using shutdown()
	 */
	public void shutdown() {
		workers.shutdown();
	}
}