import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class parses the file and prints the partially searched inverted index
 * 
 * @author shreyarajani
 * 
 */
public class IndexSearcher {

	protected LinkedHashMap<String, List<Result>> queryMapResults = new LinkedHashMap<String, List<Result>>();
	private InvertedIndex invertedIndex;

	/**
	 * Constructor to initialize the values of queryList and inverted index
	 * object
	 * 
	 * @param index
	 */
	public IndexSearcher(InvertedIndex index) {
		invertedIndex = index;
		// queryMapResults = new LinkedHashMap<String, List<Result>>();
	}

	public void addSearchResults(String line, List<Result> otherSearchResults) {
		queryMapResults.put(line, otherSearchResults);
	}

	/**
	 * This method parses the query file
	 * 
	 * @param queryFile
	 *            - the file which contains the query
	 */
	public void parseQuery(Path queryFile) {
		try (BufferedReader br = Files.newBufferedReader(queryFile,
				Charset.forName("UTF-8"));) {

			String queryline = null;
			while ((queryline = br.readLine()) != null) {
				if (queryline.trim().length() != 0) {
					queryline = queryline.trim().toLowerCase()
							.replaceAll("_", "");

					if (!queryline.isEmpty()) {
						queryMapResults.put(queryline,
								invertedIndex.partialSearch(queryline));
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("Unable to parse the file : " + queryFile);
		}
	}

	/**
	 * This method prints the partially searched inverted index
	 * 
	 * @param resultFile
	 *            - the output file of the partially searched inverted index
	 */
	public void printSearchIndex(String resultFile) {

		try (BufferedWriter br = Files.newBufferedWriter(Paths.get(resultFile),
				Charset.forName("UTF-8"));
				PrintWriter writer = new PrintWriter(br);) {

			for (String printQuery : queryMapResults.keySet()) {
				writer.write(printQuery);
				writer.println();
				for (Result result : queryMapResults.get(printQuery)) {
					writer.write(result.toString());
				}
				writer.println();
			}
		} catch (IOException ioe) {
			System.out.println("Could not write to the :" + resultFile
					+ "file.");
		}
	}
}