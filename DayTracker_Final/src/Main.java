import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the Day Tracker application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage
     */
    @Override
    public void start(Stage stage) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.initializeDatabase();

        DayTrackerView view = new DayTrackerView();
        DayTrackerController controller = new DayTrackerController(view, databaseManager);
        controller.initialize();

        Scene scene = new Scene(view.getRoot(), 1100, 700);
        stage.setTitle("Day Tracker");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX runtime.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
