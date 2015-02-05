import java.io.File;

/**
 * DirectoryTraverser is use to traverse a directory
 */
public class DirectoryTraverser {
	
	/**
	 * Traverse a file or a directory
     *
	 * @param filename the file or directory to traverse through
	 * @param index the index storage to save
	 */
	public static void traverse(String filename, IndexStorage index) {
		
		// Empty filename will cause an exception
		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Error: Filename is empty.");
		}
		// Create the file using the filename
		File file = new File(filename);
		// If the file not exists, throw an exception
		if (!file.exists()) {
			throw new IllegalArgumentException("Error: File not exists.");
		}
		walk(file, index);
	}

	/**
	 * Process a file or a directory recursively
     *
     * @param file the file to process
     * @param index the index storage to update
	 */
	private static void walk(File file, IndexStorage index) {
		System.out.println("traversing " + file.getAbsolutePath());
		// Skip hidden file or directory
		if (file.getName().startsWith(".")) {
			return;
		}
		// If is a directory
		if (file.isDirectory()) {
			// Traverse all its sub-file or sub-directory
			for (File f : file.listFiles()) {
				walk(f, index);
			}
		}
		else if (file.getName().toLowerCase().endsWith(".txt")) {
			// If is a txt file, parse it using a FileParse
			FileParser.parse(file, index);
		}
	}
}