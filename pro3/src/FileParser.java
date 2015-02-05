import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileParser {
	private WorkQueue workers;
	private int pending;
	
	/**
	 * Constructor for class FileParser
	 * @param numThreads Number of worker threads
	 */
	public FileParser(int numThreads) {
		workers = new WorkQueue(numThreads);
		pending = 0;
	}

	/**
	 * This method parses the input file for words
	 * @param ii an instance of type InvertedIndex class
	 * @param inputFile file that has to be parsed for words
	 */
	public static void findWordPosition(InvertedIndex ii, File inputFile) {
		
		String inputFilePath = inputFile.getAbsolutePath();
		BufferedReader reader = null;
		String inputLine = null;
		InvertedIndex subIndex = new InvertedIndex();
		
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			int position = 0;
			
			while((inputLine = reader.readLine())!=null) {
				String[] inputStringLine = inputLine.split("\\s");
				
				for(String word: inputStringLine) {
					word = word.toLowerCase();
					word = word.replaceAll("\\W", "");
					word = word.replaceAll("_", "");
					
					if(word != null && !word.equalsIgnoreCase("")) {
						subIndex.loadMap(word, inputFilePath, ++position);
					}
				}
			}
			reader.close();
			ii.addAll(subIndex);
		}
		
		catch(IOException e) {
			System.out.println("Cannot open file to parse words...");
		}
	}
	
	/**
	 * Method to parse the query file specified
	 * @param ii Instance of type InvertedIndex
	 * @param qFilePath query file path 
	 */
	public void parseQueryFile(InvertedIndex ii, String qFilePath) {
		BufferedReader qreader = null;
		String queryLine = null;
		
		try {
			qreader = new BufferedReader(new FileReader(qFilePath));
			
			while((queryLine = qreader.readLine()) != null) {
				workers.execute(new InvertedIndexWorker(ii, queryLine));
				
				while(getPending() > 0) {

					synchronized(this) {
						
						try {
							wait();
						} 
						catch (InterruptedException ex) {
							System.out.println("Interrupted while waiting..." + ex);
						}
					}
				}
			}
			qreader.close();
		}
		
		catch(IOException e) {
			System.out.println("Cannot open query file to read...");
		}
	}
	
	/**
	 * Method to retrieve pending
	 * @return
	 */
	private synchronized int getPending() {
		return pending;
	}
	
	/**
	 * Method to update pending
	 * @param amount
	 */
	private synchronized void updatePending(int amount) {
		pending += amount;

		if(pending <= 0) {
			notifyAll();
		}
	}
	
	/**
	 * Inner class to create an InvertedIndex worker thread for
	 * every query line parsed in the query file
	 * 
	 *
	 */
	private class InvertedIndexWorker implements Runnable {
		private String qLine = null;
		private InvertedIndex ii;
		
		public InvertedIndexWorker(InvertedIndex ind, String queryLine) {
			this.ii = ind;
			this.qLine = queryLine;
			
			updatePending(1);
		}
		
		@Override
		public void run() {
			ii.loadQueryList(qLine);
			
			updatePending(-1);
		}
	}
	
	/**
	 * To terminate threads using shutdown
	 */
	public void shutdown() {
		workers.shutdown();
	}
}