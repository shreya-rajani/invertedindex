import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Gets the word, file path and the position and adds to the outer and the inner
 * tree Maps
 * 
 * @author shreyarajani
 * 
 */

public class InvertedIndex {
	/**
	 * Declares the TreeMap - index
	 */

	private final TreeMap<String, TreeMap<String, ArrayList<Integer>>> index;

	public InvertedIndex() {
		index = new TreeMap<>();
	}

	/**
	 * Checks for the conditions and enters the word, absolute path and the
	 * position in the inverted index
	 * 
	 * @param word
	 * @param file
	 * @param pos
	 */
	public void addEntry(String word, String file, int pos) {
		TreeMap<String, ArrayList<Integer>> map1; // inner map

		if (!index.containsKey(word)) { //case 1 : TM is empty
			map1 = new TreeMap<String, ArrayList<Integer>>(); //map1= inner map
			ArrayList<Integer> list = new ArrayList<Integer>(); // list = list of positions
			list.add(pos);
			map1.put(file, list);
			index.put(word, map1);
		} else if (index.containsKey(word)) { //case 2 & 3: contains word but not the path
			map1 = index.get(word);

			if (map1.containsKey(file)) { // case 2 : has word and file, then add position
				map1.get(file).add(pos);
			} else {
				ArrayList<Integer> posList = new ArrayList<Integer>();
				posList.add(pos);
				map1.put(file, posList);
			}
		}
	}

	/**
	 * Prints the word, absolute path and the positions in the new text file
	 * 
	 * @param outfile
	 */

	public void printIndex(String outfile) {

		try (BufferedWriter br = Files.newBufferedWriter(Paths.get(outfile),
				Charset.forName("UTF-8"));
				PrintWriter writer = new PrintWriter(br);) {
			for (String word : index.keySet()) {
				writer.println(word);
				TreeMap<String, ArrayList<Integer>> inner_map = index.get(word);
				for (String fileName : inner_map.keySet()) { //file
					ArrayList<Integer> positions = inner_map.get(fileName);
					writer.print("\"" + fileName + "\"");
					for (int pos : positions) { //position
						writer.print(", " + pos);
					}
					writer.println();
				}
				writer.println();
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}
