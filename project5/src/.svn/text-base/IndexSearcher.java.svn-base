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

			String queryLine = null;
			while ((queryLine = br.readLine()) != null) {
				if (queryLine.trim().length() != 0) {
					queryLine = queryLine.trim().toLowerCase()
							.replaceAll("_", "");

					if (!queryLine.isEmpty()) {
						mapFinalQuery(queryLine);
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("Unable to parse the file : " + queryFile);
		}
	}

	public void mapFinalQuery(String queryLine) {
		queryMapResults.put(queryLine, invertedIndex.partialSearch(queryLine));
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

				System.out.println("Hi");
				System.out.println(queryMapResults.entrySet());
			}
		} catch (IOException ioe) {
			System.out.println("Could not write to the :" + resultFile
					+ "file.");
		}
	}

	public void printSearchToHtml(String user, PrintWriter writer) {
		System.out.println("[printSearchToHtml] here 1");
		for (String keys : queryMapResults.keySet()) {

			writer.println("<p>Search Results for : " + keys + "</p>");
			if (queryMapResults.get(keys).size() == 0) {
				writer.println("<p>No results found");
			}

			else {
				// System.out.println("[printSearchToHtml] query result size :"
				// + queryMapResults.get(keys).size());
				for (Result result : queryMapResults.get(keys)) {

					writer.println("<p><a href=" + result.toString() + ">"
							+ result.toString() + "</a></p>");
				}
			}
		}
		queryMapResults.clear();
	}
}