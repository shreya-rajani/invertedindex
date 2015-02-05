import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A FileParser is use to parse words from a file, process the word and send it to the callback.
 */
public class FileParser {

    /**
     * Removes all non-digit or non-letter character from a word
     *
     * @param word the word to be process
     * @return a processed word without illegal characters
     */
    private static String processWord(String word) {
        return word.replaceAll("[^0-9a-zA-Z]", "");
    }

    /**
     * Parses words from a file, and send it to the callback
     * @param f the file to parse
     * @param index the storage to update
     */
    public static void parse(File f, IndexStorage index) {
        // if file not exists, throw an exception
        if (f == null || !f.exists() || !f.isFile()) {
            throw new IllegalArgumentException("File not exists.");
        }
        Scanner in = null;
        try {
            int position = 0;
            // use a scanner to reader file
            in = new Scanner(new FileInputStream(f));
            while (in.hasNext()) {
                // read a word and process it
                String word = processWord(in.next());
                if (word != null && !word.isEmpty()) {
                    position++;
                    // if the word is not empty, save it to the index store
                    // and increase the position

                    index.addWord(word, f.getAbsolutePath(), position);
                }
            }
        }
        catch (FileNotFoundException ignored) {
        }
        finally {
            if (in != null)
                in.close();
        }
    }
}