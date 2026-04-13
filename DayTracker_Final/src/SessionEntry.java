/**
 * Represents a completed focus session.
 */
public class SessionEntry {

    private final int id;
    private final String category;
    private final int minutes;
    private final String startTime;

    /**
     * Creates a session entry.
     *
     * @param id session id
     * @param category session category
     * @param minutes session minutes
     * @param startTime session start time
     */
    public SessionEntry(int id, String category, int minutes, String startTime) {
        this.id = id;
        this.category = category;
        this.minutes = minutes;
        this.startTime = startTime;
    }

    /**
     * Gets the session id.
     *
     * @return session id
     */
    public int getId() { return id; }

    /**
     * Gets the session category.
     *
     * @return session category
     */
    public String getCategory() { return category; }

    /**
     * Gets the session duration.
     *
     * @return session minutes
     */
    public int getMinutes() { return minutes; }

    /**
     * Gets the session start time.
     *
     * @return session start time
     */
    public String getStartTime() { return startTime; }
}
