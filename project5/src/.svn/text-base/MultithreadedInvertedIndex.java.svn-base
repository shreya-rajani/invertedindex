import java.util.List;

/**
 * Multithreads the Inverted Index
 * 
 * @author shreyarajani
 * 
 */
public class MultithreadedInvertedIndex extends InvertedIndex {

	private final MultiReaderLock lock = new MultiReaderLock();

	/**
	 * Overrides the addEntry method of InvertedIndex
	 */
	@Override
	public void addEntry(String word, String file, int position) {
		lock.lockWrite();
		super.addEntry(word, file, position);
		lock.unlockWrite();
	}

	/**
	 * Overrides the printIndex method of InvertedIndex
	 */
	@Override
	public void printIndex(String outfile) {
		lock.lockRead();
		super.printIndex(outfile);
		lock.unlockRead();
	}

	/**
	 * Overrides the partialSearch method of InvertedIndex
	 */
	@Override
	public List<Result> partialSearch(String finalQuery) {
		lock.lockRead();
		List<Result> otherSearchResult = super.partialSearch(finalQuery);
		lock.unlockRead();
		return otherSearchResult;
	}

	/**
	 * Overrides the addAll method of InvertedIndex
	 */
	public void addAll(InvertedIndex other) {
		lock.lockWrite();
		super.addAll(other);
		lock.unlockWrite();
	}
}
