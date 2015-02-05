import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Parses and extract words from the given text file
 * 
 * @author shreyarajani
 * 
 */
public class InvertedIndexBuilder {

	/**
	 * This method parses the files and then normalizes the string, separates
	 * the words
	 * 
	 * @param file
	 *            - parses the file from which the words are to be extracted
	 * @param invertedIndex
	 *            - the object of the class Inverted Index so that the words,
	 *            file and position can be added in it
	 */
	public void parseFile(Path file, InvertedIndex invertedIndex) {

		String fileName = file.toString();

		try (BufferedReader br = Files.newBufferedReader(file,
				Charset.forName("UTF-8"));) {

			int indexPosition = 0;
			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.trim().length() != 0) {
					String[] wordsArray = line.split("\\s");
					for (String word : wordsArray) {
						word = word.trim().toLowerCase().replaceAll("\\W", "")
								.replaceAll("_", "");
						if (!word.isEmpty()) {
							indexPosition = indexPosition + 1;
							invertedIndex.addEntry(word, fileName,
									indexPosition);
						}
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("Could not read File : " + fileName);
		}
	}

	/**
	 * Adds the files to the inverted index
	 * 
	 * @param files
	 *            - List of all the paths of the words found
	 * @param invertedIndex
	 *            - InvertedIndex object
	 */
	public void parseFiles(ArrayList<Path> files,
			InvertedIndex invertedIndex) {
		for (Path file : files) {
			parseFile(file, invertedIndex);
		}
	}
}
