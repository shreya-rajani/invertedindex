import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This is the Driver class which takes in the argument from the console and
 * then separates the flags and the name and it calls the other functions
 * 
 * @author shreyarajani
 * 
 */

public class Driver {
	/**
	 * Calls all the methods and checks for -d and -i flags
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArgumentParser ap = new ArgumentParser(args);
		/**
		 * checks if it it has -d and processes the path and adds to Inverted
		 * Index
		 */
		if (ap.hasFlag("-d")) {
			String dir = ap.getValue("-d");
			Path path = Paths.get(dir);
			ArrayList<Path> fileList = DirectoryTraverser.traverse(path);
			InvertedIndex ii = new InvertedIndex();
			InvertedIndexBuilder.parseFiles(fileList, ii);

			/**
			 * Checks of -i and passes the arguments after -i to printTMap to
			 * create a text file with that name or else with a default name
			 * invertedindex.txt
			 */
			if (ap.hasFlag("-i")) {
				String outfile = ap.getValue("-i");
				ii.printIndex(outfile);
			} else {
				ii.printIndex("invertedindex.txt");
			}
		} else {
			System.out.println("Error: Invalid input");
			System.out.println("Usage: -d <directory> -i <filename>");
		}
	}
}