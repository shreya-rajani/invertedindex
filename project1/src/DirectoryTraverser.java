import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Browses the directory recursively and returns all the texts files to
 * FilePArser for processing
 * 
 * @author shreyarajani
 * 
 */

public class DirectoryTraverser {
	/**
	 * Creates a ArrayList that stores Path of the files that are to be
	 * traversed
	 * 
	 * @param root
	 * @return
	 */
	public static ArrayList<Path> traverse(Path root) {
		ArrayList<Path> files = new ArrayList<>();
		traverse(root, files);
		return files;
	}

	/**
	 * Traverses the directory and calls this function recursively to separate
	 * the text file and parse it.
	 * 
	 * @param path
	 * @param files
	 */
	private static void traverse(Path path, ArrayList<Path> files) {

		if (Files.isDirectory(path)) {
			try {
				for (Path file : Files.newDirectoryStream(path)) {
					traverse(file, files);
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		} else {
			if (path.toString().toLowerCase().endsWith(".txt")) {
				files.add(path);
			}
		}
	}
}