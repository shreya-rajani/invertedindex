import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * The driver to start the program
 */
public class Driver {

    public static void main(String[] args) {
        String inputFile = null;
        String outputFile = "invertedindex.txt";
        String resultFile = "searchresults.txt";
        String queryFile = null;        /*
         * TODO REVIEW:
		 * Use your ArgumentParser from homework!
		 * STILL DO THIS!
		 */

        // Parse arguments
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-d")) {
                inputFile = args[i + 1];
            } else
                if (args[i].equals("-i")) {
                    outputFile = args[i + 1];
                } else
                    if (args[i].equals("-r")) {
                        resultFile = args[i + 1];
                    } else
                        if (args[i].equals("-q")) {
                            queryFile = args[i + 1];
                        }
        }

        try {
            // Create an index storage
            final IndexStorage storage = new IndexStorage();
            DirectoryTraverser.traverse(inputFile, storage);
            saveIndex(storage, outputFile);

            // Saves the query result to the file
            List<QueryResult> result = IndexFinder.query(storage, queryFile);
            saveQueryResult(result, outputFile);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Saves the query result to the file
     * @param results list of query result
     * @param resultFile the filename of the output file
     */
    private static void saveQueryResult(List<QueryResult> results, String resultFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(resultFile));
            for (QueryResult result : results) {
                writer.write(result + "\n");
            }
            writer.write("\n");
            writer.flush();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Saves the index to the file
     * @param index the index storage to be saved
     * @param filename the filename of the output file
     */
    public static void saveIndex(IndexStorage index, String filename) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(filename));
            for (Map.Entry<String, Index> entry : index.getIndexMap().entrySet()) {
                writer.write(entry.getKey() + "\n");
                writer.write(entry.getValue().toString() + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}