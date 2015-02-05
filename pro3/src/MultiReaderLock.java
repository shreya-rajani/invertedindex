/**
 * Custom lock class 
 * 
 *
 */
public class MultiReaderLock {
	
	private int reader, writer;
	
	/**
	 * Constructor for class MultiReaderLock
	 */
	public MultiReaderLock() {
		reader = 0;
		writer = 0;
	}
	
	/**
	 * Method used to obtain a read lock
	 * A read lock is given if and only if there
	 * are no writers currently executing
	 */
	public synchronized void acquireReadLock() {

		while(writer > 0) {
			
			try{
				wait();
			}
			catch(InterruptedException ex) { }
			
		}
		
		reader++;
	}
	
	/**
	 * Method to release the read lock
	 */
	public synchronized void releaseReadLock() {
		
		reader--;
		notifyAll();
	}
	
	/**
	 * Method used to obtain a write lock
	 * A write lock is given if and only if there are
	 * no writers having another write lock
	 */
	public synchronized void acquireWriteLock() {
		while(writer > 0 || reader > 0) {
			
			try{
				wait();
			}
			catch(InterruptedException ex) { }
			
		}
		
		writer++;
	}
	
	/**
	 * Method to release the write lock
	 */
	public synchronized void releaseWriteLock() {
		
		writer--;
		notifyAll();
	}
	
}
