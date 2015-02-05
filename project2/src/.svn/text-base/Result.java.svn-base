/**
 * Class that compares the frequency and position and updates it
 * 
 * @author shreyarajani
 * 
 */
public class Result implements Comparable<Result> {

	private String path;
	private int frequency;
	private int position;

	/**
	 * Constructor to initialize the frequency, path and position
	 * 
	 * @param path
	 */
	public Result(String path) {
		this.path = path;
		frequency = 0;
		position = Integer.MAX_VALUE;
	}

	/**
	 * Method compares frequencies and position and decides which to use in the
	 * partial search
	 */
	@Override
	public int compareTo(Result otherResult) {

		if (getFrequency() < otherResult.frequency) {
			return 1;
		} else if (getFrequency() > otherResult.frequency) {
			return -1;
		} else {
			if (otherResult.position < getPosition()) {
				return 1;
			} else if (otherResult.position > getPosition()) {
				return -1;
			} else {
				return String.CASE_INSENSITIVE_ORDER.compare(getPath(),
						otherResult.path);
			}
		}
	}

	/**
	 * Gets the value of path
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the value for path
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the value of frequency
	 * 
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Sets the value for Frequency
	 * 
	 * @param frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * Gets the value of position
	 * 
	 * @return position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the value for Position
	 * 
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * updates the value for frequency and position
	 * 
	 * @param otherFrequency
	 * @param otherPosition
	 */
	public void update(int otherFrequency, int otherPosition) {
		this.frequency = this.frequency + otherFrequency;

		if (getPosition() > otherPosition) {
			this.position = otherPosition;
		}
	}

	/**
	 * toString Method that returns the string of the formatted output
	 */
	@Override
	public String toString() {
		return String.format("\"%s\", %d, %d\n", getPath(), getFrequency(),
				getPosition());
	}
}