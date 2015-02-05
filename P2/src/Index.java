import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Index class is used to save the file path and positionMap where word was found
 */
public class Index {
    // A map to save the position of the word
    // the key is file path and the value is a list of positons
    private Map<String, List<Integer>> positionMap;

    // The constructor is used to initialize the map
    public Index() {

        // Use TreeMap to keep the key in lexicographical order
        positionMap = new TreeMap<String, List<Integer>>();
    }

    /**
     * Add a file path and a position to the word index
     *
     * @param path the path of the word to add
     * @param position the position of the word in the parse
     */
    public void add(String path, int position) {
        // If the path not exists in the position map
        if (!positionMap.containsKey(path)) {
            positionMap.put(path, new ArrayList<Integer>());
        }
        // Add the position to the path
        positionMap.get(path).add(position);
    }

    /**
     * @return a string contains all information of this word index
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Loop for all path-positions entry pair
        for (Map.Entry<String, List<Integer>> entry : positionMap.entrySet()) {
            // file path
            builder.append("\"").append(entry.getKey()).append("\"");
            // positions
            for (int pos : entry.getValue()) {
                builder.append(", ").append(pos);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * @return a map of all the positions
     */
    public Map<String, List<Integer>> getPositionMap() {
        return positionMap;
    }
}