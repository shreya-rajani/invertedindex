
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
 
public class ReadFile {
 
	public static void main(String[] args) {
 
		File file = new File("demo.txt");
		FileInputStream fis = null;
 
		System.out.println(file.getAbsolutePath());
		try {
			fis = new FileInputStream(file);
 
			System.out.println("Total file size to read (in bytes) : "
					+ fis.available());
 
			int content;
			while ((content = fis.read()) != -1) {
				// convert to char and display it
				System.out.print((char) content);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}