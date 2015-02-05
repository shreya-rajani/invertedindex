/**
 * This class is used to hold the result of query.
 */
public class QueryResult implements Comparable<QueryResult> {
    private String path;
    private int frequency;
    private int position;

    public QueryResult(String path, int frequency, int position) {
        this.path = path;
        this.frequency = frequency;
        this.position = position;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Updates the frequency and position
     */
    public void update(int frequency, int position) {
        this.frequency = frequency;
        this.position = position;
    }

    @Override
    public String toString() {
        return "\"" + path + "\", " + frequency + ", " + position;
    }

    /**
     * Compares to another query result
     */
    @Override
    public int compareTo(QueryResult q) {
        if (this.getFrequency() != q.getFrequency())
            return this.getFrequency() < q.getFrequency() ? 1 : -1;
        else if (this.getPosition() != q.getPosition())
            return this.getPosition() > q.getPosition() ? 1 : -1;
        return 0;
    }
}
