import java.io.*;
import java.util.*;
import java.util.Comparator;

/**
 * A index storage to save word and index information pair
 */
public class IndexStorage {
    // use a map to save the relationship between word and its index info

    /*
     * TODO REVIEW:
     * Combine Index and IndexMap into a single data structure class named
     * InvertedIndex. Just make indexMap a triply-nested data structure.
     */
    private Map<String, Index> indexMap;

    /**
     * constructor used to initialize the map
     */
    public IndexStorage() {
        // use TreeMap to keep the key in lexicographical order
        indexMap = new TreeMap<String, Index>();
    }

    /**
     * add a word the index map
     *
     * @param word     the word to be added
     * @param path     path of the file
     * @param position position in the file
     */
    public void addWord(String word, String path, int position) {
        // if the map does not contain the word
        if (!indexMap.containsKey(word)) {
            // put it into the map with an empty index
            indexMap.put(word, new Index());
        }
        // add the path and position to the index of the word
        indexMap.get(word).add(path, position);
    }

    /**
     * Prints all the keys
     */
    public void print() {
        for (Map.Entry<String, Index> entry : indexMap.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    /**
     * @return the index map
     */
    public Map<String, Index> getIndexMap() {
        return indexMap;
    }
}