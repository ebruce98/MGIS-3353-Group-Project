import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles SQLite database operations for the Day Tracker application.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:daytracker.db";

    /**
     * Opens a database connection.
     *
     * @return SQLite connection
     * @throws SQLException if the connection fails
     */
    public Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found. Add sqlite-jdbc.jar to the classpath.", e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates required tables if they do not already exist.
     */
    public void initializeDatabase() {
        String createTasks = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    minutes INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    notes TEXT,
                    completed INTEGER NOT NULL
                )
                """;

        String createSessions = """
                CREATE TABLE IF NOT EXISTS sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category TEXT NOT NULL,
                    minutes INTEGER NOT NULL,
                    start_time TEXT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createTasks);
            statement.execute(createSessions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a task into the database.
     *
     * @param task task to insert
     */
    public void insertTask(Task task) {
        String sql = "INSERT INTO tasks(name, category, minutes, date, notes, completed) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, task.getName());
            statement.setString(2, task.getCategory());
            statement.setInt(3, task.getMinutes());
            statement.setString(4, task.getDate());
            statement.setString(5, task.getNotes());
            statement.setInt(6, task.isCompleted() ? 1 : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns every stored task.
     *
     * @return list of tasks
     */
    public java.util.List<Task> getAllTasks() {
        java.util.List<Task> results = new java.util.ArrayList<>();
        String sql = "SELECT id, name, category, minutes, date, notes, completed FROM tasks ORDER BY id DESC";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                results.add(new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getInt("minutes"),
                        resultSet.getString("date"),
                        resultSet.getString("notes"),
                        resultSet.getInt("completed") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Updates the completion status for a task.
     *
     * @param taskId task id
     * @param completed new completion value
     */
    public void updateTaskCompletion(int taskId, boolean completed) {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, completed ? 1 : 0);
            statement.setInt(2, taskId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a task.
     *
     * @param taskId id of the task to delete
     */
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a completed focus session.
     *
     * @param sessionEntry session to insert
     */
    public void insertSession(SessionEntry sessionEntry) {
        String sql = "INSERT INTO sessions(category, minutes, start_time) VALUES(?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionEntry.getCategory());
            statement.setInt(2, sessionEntry.getMinutes());
            statement.setString(3, sessionEntry.getStartTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns every stored session.
     *
     * @return list of sessions
     */
    public java.util.List<SessionEntry> getAllSessions() {
        java.util.List<SessionEntry> results = new java.util.ArrayList<>();
        String sql = "SELECT id, category, minutes, start_time FROM sessions ORDER BY id DESC";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                results.add(new SessionEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getInt("minutes"),
                        resultSet.getString("start_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
