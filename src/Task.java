import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a planner task.
 */
public class Task {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final IntegerProperty minutes;
    private final StringProperty date;
    private final StringProperty notes;
    private final BooleanProperty completed;

    /**
     * Creates a task object.
     *
     * @param id task id
     * @param name task name
     * @param category task category
     * @param minutes estimated minutes
     * @param date task date
     * @param notes task notes
     * @param completed completion flag
     */
    public Task(int id, String name, String category, int minutes, String date, String notes, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.minutes = new SimpleIntegerProperty(minutes);
        this.date = new SimpleStringProperty(date);
        this.notes = new SimpleStringProperty(notes);
        this.completed = new SimpleBooleanProperty(completed);
    }

    /**
     * Gets the task id.
     *
     * @return task id
     */
    public int getId() { return id.get(); }

    /**
     * Gets the task id property.
     *
     * @return id property
     */
    public IntegerProperty idProperty() { return id; }

    /**
     * Gets the task name.
     *
     * @return task name
     */
    public String getName() { return name.get(); }

    /**
     * Gets the task name property.
     *
     * @return name property
     */
    public StringProperty nameProperty() { return name; }

    /**
     * Gets the category.
     *
     * @return task category
     */
    public String getCategory() { return category.get(); }

    /**
     * Gets the category property.
     *
     * @return category property
     */
    public StringProperty categoryProperty() { return category; }

    /**
     * Gets the estimated minutes.
     *
     * @return estimated minutes
     */
    public int getMinutes() { return minutes.get(); }

    /**
     * Gets the minutes property.
     *
     * @return minutes property
     */
    public IntegerProperty minutesProperty() { return minutes; }

    /**
     * Gets the task date.
     *
     * @return task date
     */
    public String getDate() { return date.get(); }

    /**
     * Gets the date property.
     *
     * @return date property
     */
    public StringProperty dateProperty() { return date; }

    /**
     * Gets the notes.
     *
     * @return notes
     */
    public String getNotes() { return notes.get(); }

    /**
     * Gets the notes property.
     *
     * @return notes property
     */
    public StringProperty notesProperty() { return notes; }

    /**
     * Checks whether the task is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() { return completed.get(); }

    /**
     * Gets the completed property.
     *
     * @return completed property
     */
    public BooleanProperty completedProperty() { return completed; }

    /**
     * Sets the completion flag.
     *
     * @param value new completion value
     */
    public void setCompleted(boolean value) { completed.set(value); }
}
