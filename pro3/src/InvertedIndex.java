import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *  Class to store and manage the Inverted Index
 *
 *
 */
public class InvertedIndex {
	
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> indexMap;
	private ArrayList<SearchResults> queryList;
	private MultiReaderLock searchResultsLock;
	private MultiReaderLock iiLock;
	
	/**
	 * Constructor for the class MapManager
	 * @throws IOException
	 */
	public InvertedIndex() {
		indexMap = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
		queryList = new ArrayList<SearchResults>();
		searchResultsLock = new MultiReaderLock();
		iiLock = new MultiReaderLock();
	}
	
	/**
	 * Class that processes all the input files
	 * that contain the query words
	 * 
	 *
	 */
	private class SearchResults {
		private String query;
		private HashMap<String, FrequencyCompare> results;
		private ArrayList<FrequencyCompare> searchResultList;
		
		public SearchResults(String queryLine) {
			results = new HashMap<String, FrequencyCompare>();
			this.query = queryLine;
			String[] queries = queryLine.split("\\s");
			
			for(String queryWord: queries) {
				queryWord = queryWord.toLowerCase().replaceAll("\\W", "").replaceAll("_", "");
				
				for(String word = indexMap.ceilingKey(queryWord); word != null; word = indexMap.higherKey(word)) {
					
					if(!word.startsWith(queryWord)) {
						break;
					}
						
					for(String path: indexMap.get(word).keySet()) {
							
						if(results.containsKey(path)) {
							results.get(path).updateFrequencyPosition(word, path);
						}
							
						else {
							results.put(path, new FrequencyCompare(word, path));
						}
					}
				}
			}
			Collections.sort(searchResultList = new ArrayList<FrequencyCompare>(results.values()));
		}
		
		/**
		 * Class to rank the files according to the frequency of occurrence and
		 * the position where the query words are found
		 * 
		 *
		 */
		private class FrequencyCompare implements Comparable<FrequencyCompare> {
			private String path;
			private int frequency;
			private int firstPos;
			
			public FrequencyCompare(String word, String path) {
				this.path = path;
				this.frequency = indexMap.get(word).get(path).size();
				this.firstPos = indexMap.get(word).get(path).first();
			}

			@Override
			public int compareTo(FrequencyCompare other) {
				
				if(this.frequency < other.frequency) {
					return 1;
				}
				
				else if(this.frequency > other.frequency){
					return -1;
				}
				
				else {
					
					if(this.firstPos < other.firstPos) {
						return -1;
					}
					
					else if(this.firstPos == other.firstPos) {
						return String.CASE_INSENSITIVE_ORDER.compare(this.path, other.path);
					}
					
					else {
						return 1;
					}
				}
			}
			
			/**
			 * Method to update the frequencies and the first position of
			 * occurrence in the HashMap, in case the path is already present in it
			 * @param word the word found in the Inverted Index Map for which the
			 * frequencies should be updated
			 */
			public void updateFrequencyPosition(String word, String path) {
				
				this.frequency = this.frequency + indexMap.get(word).get(path).size();
				int firstPosition = indexMap.get(word).get(path).first();
				
				if(this.firstPos > firstPosition) {
					this.firstPos = firstPosition;
				}
			}
		}
	}
	
	/**
	 * Method to add the queries to a list of queries
	 * as and when they are parsed by the FileParser
	 * @param queryLine each query line parsed from the query file
	 */
	public void loadQueryList(String queryLine) {
		searchResultsLock.acquireWriteLock();
		queryList.add(new SearchResults(queryLine));
		searchResultsLock.releaseWriteLock();
	}
	
	/**
	 * Method to load the Inverted Index map (indexMap) as and when a new position for an input word is found
	 * @param inputWord word that is to be stored in the Inverted Index
	 * @param inputPath path which is mapped with the word
	 * @param position the position of the word found in the file
	 * @throws IOException
	 */
	public void loadMap(String inputWord, String inputPath, int position) {
		iiLock.acquireWriteLock();
		
		if(!indexMap.containsKey(inputWord)) {
			indexMap.put(inputWord, new TreeMap<String, TreeSet<Integer>>());
			indexMap.get(inputWord).put(inputPath, new TreeSet<Integer>());
			indexMap.get(inputWord).get(inputPath).add(position);
		}
		
		else if(!(indexMap.get(inputWord).containsKey(inputPath))) {
			indexMap.get(inputWord).put(inputPath, new TreeSet<Integer>());
			indexMap.get(inputWord).get(inputPath).add(position);
		}
		
		else if (indexMap.containsKey(inputWord) && indexMap.get(inputWord).containsKey(inputPath)) {
			indexMap.get(inputWord).get(inputPath).add(position);
		}
		iiLock.releaseWriteLock();
	}
	
	/**
	 * Method to add contents of subIndex
	 * @param subIndex
	 */
	public void addAll(InvertedIndex subIndex) {
		iiLock.acquireWriteLock();
		
		for(String word: subIndex.indexMap.keySet()) {
			
			for(String path: subIndex.indexMap.get(word).keySet()) {
			
				if(!indexMap.containsKey(word)) {
					indexMap.put(word, new TreeMap<String, TreeSet<Integer>>());
					indexMap.get(word).put(path, new TreeSet<Integer>());
					indexMap.get(word).get(path).addAll(subIndex.indexMap.get(word).get(path));
				}
			
				else if(indexMap.containsKey(word) && !(indexMap.get(word).containsKey(path))) {
					indexMap.get(word).put(path, new TreeSet<Integer>());
					indexMap.get(word).get(path).addAll(subIndex.indexMap.get(word).get(path));
				}
			
				else if(indexMap.containsKey(word) && (indexMap.get(word).containsKey(path))) {
					indexMap.get(word).get(path).addAll(subIndex.indexMap.get(word).get(path));
				}
			}
		}
		iiLock.releaseWriteLock();
	}
	
	/**
	 * Method to write the search results into the file "searchresults.txt"
	 * @throws IOException
	 */
	public void writeSearchResults() throws IOException {
		searchResultsLock.acquireReadLock();
		File outputFile = new File("searchresults.txt");
		PrintWriter writer  = null;
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
			
			for(SearchResults sr: queryList) {
				
				if(sr != queryList.iterator().next()) {
					writer.println();
				}
				writer.println(sr.query);
				
				for(SearchResults.FrequencyCompare fc: sr.searchResultList) {
					writer.print("\"" + fc.path + "\", " + fc.frequency + ", " + fc.firstPos + "\n");
				}
			}
		}
		
		catch(IOException exception) {
			throw new IOException("Unable to write to \"searchresults.txt\"...");
		}
		
		finally {
			try {
				writer.flush();
				writer.close();
				searchResultsLock.releaseReadLock();
			}
			catch(Exception exp) {}
		}
	}

	/**
	 * Method to write the Inverted Index map into the file "invertedindex.txt"
	 * @throws IOException
	 */
	public void writeInvertedIndex() throws IOException {
		
		iiLock.acquireReadLock();
		File outputFile = new File("invertedindex.txt");
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
			
			for(String word: indexMap.keySet()) {
				writer.println(word);
				
				for(String path: indexMap.get(word).keySet()) {
					
					if(!(path == indexMap.get(word).firstKey())) {
						writer.println();
					}
					
					writer.print("\"" + path + "\", ");
					Iterator<Integer> itr = indexMap.get(word).get(path).iterator();
					writer.print(itr.next());
					
					while(itr.hasNext()) {
						writer.print(", " + itr.next());
					}
				}
				writer.println("\n");
			}
		}
		
		catch(IOException exception) {
			throw new IOException("Unable to write to \"invertedindex.txt\"...");
		}
		
		finally {
			
			try {
				writer.flush();
				writer.close();
				iiLock.releaseReadLock();
			}
			catch(Exception exp) {}
		}
	}
}