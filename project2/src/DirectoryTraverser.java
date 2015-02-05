import java.io.FilePermission;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
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
	
	InvertedIndexBuilder fParse = new InvertedIndexBuilder();
	InvertedIndex ii;
	
	public DirectoryTraverser(InvertedIndex ii){
		this.ii = ii;
		this.fParse = new InvertedIndexBuilder();
	}
	/**
	 * Traverses the directory and calls this function recursively to separate
	 * the text file and parse it.
	 * 
	 * @param path
	 *            - its the input path
	 * 
	 * @param files
	 *            - The output files after traversal
	 */
	public void traverse(Path path) {

		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
				for (Path file : Files.newDirectoryStream(path)) {
					traverse(file);
				}
			} catch (IOException | DirectoryIteratorException e) {
				System.out.println("Directory" + path + "not found."
						+ e.getMessage());
			}
		} else {
			if (path.toString().toLowerCase().endsWith(".txt")) {
				fParse.parseFile(path, ii);
			}
		}
	}
}