import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Gets the word, file path and the position and adds to the outer and the inner
 * tree Maps
 * 
 * Now it also searches the inverted index partially
 * 
 * @author shreyarajani
 * 
 */
public class InvertedIndex {

	private final TreeMap<String, TreeMap<String, ArrayList<Integer>>> index;

	public InvertedIndex() {
		index = new TreeMap<>();
	}

	/**
	 * Checks for the conditions and enters the word, absolute path and the
	 * position in the inverted index
	 * 
	 * @param word
	 *            - parsed and stored in the inverted index
	 * @param file
	 *            - the path of the word where it is found
	 * @param position
	 *            - the position of the word in the file
	 */
	public void addEntry(String word, String file, int position) {
		TreeMap<String, ArrayList<Integer>> innermap;

		if (!index.containsKey(word)) {
			innermap = new TreeMap<String, ArrayList<Integer>>();
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(position);
			innermap.put(file, list);
			index.put(word, innermap);
		} else if (index.containsKey(word)) {
			innermap = index.get(word);

			if (innermap.containsKey(file)) {
				innermap.get(file).add(position);
			} else {
				ArrayList<Integer> positionList = new ArrayList<Integer>();
				positionList.add(position);
				innermap.put(file, positionList);
			}
		}
	}

	/**
	 * Prints the word, absolute path and the positions in the new text file
	 * 
	 * @param outputfile
	 *            - the name of the output file the user wants
	 */
	public void printIndex(String outfile) {
		try (BufferedWriter br = Files.newBufferedWriter(Paths.get(outfile),
				Charset.forName("UTF-8"));
				PrintWriter writer = new PrintWriter(br);) {
			for (String word : index.keySet()) {
				writer.println(word);
				TreeMap<String, ArrayList<Integer>> innerMap2 = index.get(word);
				for (String fileName : innerMap2.keySet()) {
					ArrayList<Integer> positions = innerMap2.get(fileName);
					writer.print("\"" + fileName + "\"");
					for (int finalPosition : positions) {
						writer.print(", " + finalPosition);
					}
					writer.println();
				}
				writer.println();
			}
		} catch (IOException ioe) {
			System.out.println("Unable to write to file : " + outfile);
		}
	}

	/**
	 * Searches the inverted index from the query the user has given
	 * 
	 * @param queryLine
	 *            - the query line of the query file given by the user
	 * @return resultsMap - Map that contains the path the results object
	 */
	public List<Result> partialSearch(String queryLine) {
		final HashMap<String, Result> resultsMap = new HashMap<String, Result>();

		ArrayList<Integer> ranksList;
		Result result, queryFinalPath;

		String[] queryWords = queryLine.split("\\s");
		for (String queryWord : queryWords) {
			for (String word : index.tailMap(queryWord).keySet()) {
				if (!word.startsWith(queryWord)) {
					break;
				} else if (word.startsWith(queryWord)) {
					for (String path : index.get(word).keySet()) {
						ranksList = index.get(word).get(path);
						queryFinalPath = resultsMap.get(path);

						if (resultsMap.containsKey(path)) {
							result = queryFinalPath;
						} else {
							result = new Result(path);
						}
						result.update(ranksList.size(), ranksList.get(0));
						resultsMap.put(path, result);
					}
				}
			}
		}

		List<Result> searchList = new ArrayList<Result>(resultsMap.values());
		Collections.sort(searchList);
		return searchList;
	}

	private TreeMap<String, TreeMap<String, ArrayList<Integer>>> otherInvertedIndex() {
		return this.index;
	}

	/**
	 * temporary map method for multi threaded inverted index
	 * 
	 * @param otherinvertedIndex
	 *            -object of invertedindex
	 */
	public void addAll(InvertedIndex otherinvertedIndex) {
		for (String key : otherinvertedIndex.index.keySet()) {
			if (!this.index.containsKey(key)) {
				this.index.put(key, otherinvertedIndex.index.get(key));
			} else {
				index.get(key).putAll(
						otherinvertedIndex.otherInvertedIndex().get(key));
			}
		}
	}
}