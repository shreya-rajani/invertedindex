import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class IndexFinder {

    /**
     * Returns true if the index starts with one of the queries
     *
     * @param queries an array of queries to check
     * @param index word of the index
     */
    private static boolean hitQuery(String[] queries, String index) {
        for (String query : queries)
            if (index.startsWith(query))
                return true;
        return false;
    }

    /**
     * Returns a sorted query results
     */
    private static List<QueryResult> query(IndexStorage index, String[] words) {
        Map<String, QueryResult> queryResultMap = new HashMap<String, QueryResult>();
        for (Map.Entry<String, Index> entry : index.getIndexMap().entrySet())
            if (hitQuery(words, entry.getKey())) {
                // If hit the query, update the map of query result
                for (Map.Entry<String, List<Integer>> entry2 : entry.getValue().getPositionMap().entrySet()) {
                    if (!queryResultMap.containsKey(entry2.getKey())) {
                        queryResultMap.put(entry2.getKey(), new QueryResult(entry2.getKey(),
                            entry2.getValue().size(), entry2.getValue().get(0)));
                    } else {
                        QueryResult result = queryResultMap.get(entry2.getKey());
                        result.update(result.getFrequency() + entry2.getValue().size(),
                            Math.min(result.getPosition(), entry2.getValue().get(0)));
                    }
                }
            }
        List<QueryResult> results = new ArrayList<QueryResult>(queryResultMap.values());
        // Sort the result list according to the rank rules.
        Collections.sort(results);
        return results;
    }

    /**
     * Returns a sorted query results
     */
    public static List<QueryResult> query(IndexStorage index, String queryFile) {
        List<QueryResult> results = new ArrayList<QueryResult>();
        Scanner reader = null;
        try {
            reader = new Scanner(new FileInputStream(queryFile));
            while (reader.hasNextLine()) {
                // Read a line of query words
                String line = reader.nextLine();
                // Split into words array.
                String[] words = line.split("\\s+");
                results.addAll(IndexFinder.query(index, words));
            }
            return results;
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return results;
        } finally {
            if (reader != null)
                reader.close();
        }
    }

}
