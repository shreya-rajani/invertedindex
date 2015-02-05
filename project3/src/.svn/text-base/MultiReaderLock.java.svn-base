/**
 * A simple custom lock that allows simultaneously read operations, but
 * disallows simultaneously write and read/write operations.
 * 
 * You do not need to implement any form or priority to read or write
 * operations. The first thread that acquires the appropriate lock should be
 * allowed to continue.
 * 
 * @author CS 212 Software Development
 * @author University of San Francisco
 */
public class MultiReaderLock {

	private int setReader;
	private int setWriter;

	/**
	 * Initializes a multi-reader (single-writer) lock.
	 */
	public MultiReaderLock() {

		setReader = 0;
		setWriter = 0;
	}

	/**
	 * Will wait until there are no active writers in the system, and then will
	 * increase the number of active readers.
	 */
	public synchronized void lockRead() {

		while (setWriter > 0) {
			try {
				wait();
			} catch (InterruptedException ie) {
				System.out.println(ie);
			}
		}
		setReader++;
	}

	/**
	 * Will decrease the number of active readers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockRead() {

		setReader--;
		notifyAll();
	}

	/**
	 * Will wait until there are no active readers or writers in the system, and
	 * then will increase the number of active writers.
	 */
	public synchronized void lockWrite() {

		while (setWriter > 0 || setReader > 0) {
			try {
				wait();
			} catch (InterruptedException ie) {
				System.out.println(ie);
			}
		}
		setWriter++;
	}

	/**
	 * Will decrease the number of active writers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockWrite() {
		setWriter--;
		notifyAll();

	}
}