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
	 * @param ii
	 */

	public static void parseFile(Path file, InvertedIndex ii) {

		try (BufferedReader br = Files.newBufferedReader(file,
				Charset.forName("UTF-8"));) {

			String fileName = file.toString();

			int pos = 0;
			String line = null;

			while ((line = br.readLine()) != null) {

				if (line.trim().length() != 0) {
					String[] ar = line.split(" ");

					for (String ss : ar) {

						ss = ss.trim();
						ss = ss.toLowerCase();
						ss = ss.replaceAll("\\W", "");
						ss = ss.replaceAll("_", "");

						if (!ss.isEmpty()) {
							pos = pos + 1;
							ii.addEntry(ss, fileName, pos);
						}
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	/**
	 * Calls the parseFile and adds it to the inverted index
	 * 
	 * @param files
	 * @param ii
	 */
	public static void parseFiles(ArrayList<Path> files, InvertedIndex ii) {
		for (Path file : files) {
			parseFile(file, ii);
		}
	}
}
