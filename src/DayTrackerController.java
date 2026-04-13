import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Coordinates user actions, view updates, and database operations.
 */
public class DayTrackerController {

    private final DayTrackerView view;
    private final DatabaseManager databaseManager;
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final ObservableList<SessionEntry> sessions = FXCollections.observableArrayList();
    private final IntegerProperty remainingSeconds = new SimpleIntegerProperty(25 * 60);
    private final IntegerProperty plannedMinutes = new SimpleIntegerProperty(25);
    private final BooleanProperty timerRunning = new SimpleBooleanProperty(false);
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");

    private Timeline timeline;

    /**
     * Creates the controller.
     *
     * @param view application view
     * @param databaseManager database helper
     */
    public DayTrackerController(DayTrackerView view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    /**
     * Initializes event handlers, timer behavior, and starting data.
     */
    public void initialize() {
        view.getTaskTable().setItems(tasks);
        view.getTodayDateLabel().setText("Today: " + LocalDate.now());

        view.bindTimerLabel(Bindings.createStringBinding(
                () -> formatSeconds(remainingSeconds.get()),
                remainingSeconds
        ));

        initializeTimer();
        registerHandlers();
        loadDataFromDatabase();
        refreshSessionHistory();
        refreshSummary();
    }

    private void registerHandlers() {
        view.getAddTaskButton().setOnAction(event -> handleAddTask());
        view.getRemoveSelectedButton().setOnAction(event -> handleRemoveSelectedTask());
        view.getMarkDoneButton().setOnAction(event -> handleToggleComplete());
        view.getRefreshSessionViewButton().setOnAction(event -> refreshSessionHistory());
        view.getRefreshSummaryButton().setOnAction(event -> refreshSummary());

        view.getStartButton().setOnAction(event -> handleStartSession());
        view.getPauseButton().setOnAction(event -> handlePauseSession());
        view.getResetButton().setOnAction(event -> handleResetSession());
        view.getSaveSessionButton().setOnAction(event -> handleSaveSession());

        view.getSessionLengthBox().setOnAction(event -> {
            if (!timerRunning.get()) {
                plannedMinutes.set(view.getSessionLengthBox().getValue());
                remainingSeconds.set(plannedMinutes.get() * 60);
                view.getTimerProgressBar().setProgress(1.0);
            }
        });
    }

    private void initializeTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (remainingSeconds.get() > 0) {
                remainingSeconds.set(remainingSeconds.get() - 1);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        remainingSeconds.addListener((obs, oldVal, newVal) -> {
            int total = plannedMinutes.get() * 60;
            if (total > 0) {
                view.getTimerProgressBar().setProgress((double) newVal.intValue() / total);
            }

            if (newVal.intValue() == 0 && oldVal.intValue() > 0) {
                timeline.stop();
                timerRunning.set(false);

                SessionEntry sessionEntry = new SessionEntry(
                        0,
                        view.getSessionCategoryBox().getValue(),
                        plannedMinutes.get(),
                        LocalTime.now().format(timeFormat)
                );

                databaseManager.insertSession(sessionEntry);
                loadSessionsFromDatabase();
                refreshSessionHistory();
                refreshSummary();

                showAlert("Session Complete", "Your focus session is complete.");
            }
        });
    }

    private void handleAddTask() {
        String name = view.getTaskNameField().getText().trim();
        String category = view.getPlannerCategoryBox().getValue();
        int minutes = view.getMinutesSpinner().getValue();
        LocalDate date = view.getDatePicker().getValue();
        String notes = view.getNotesArea().getText().trim();

        if (name.isEmpty()) {
            showAlert("Validation Error", "Please enter a task name.");
            return;
        }

        if (category == null || category.isEmpty()) {
            showAlert("Validation Error", "Please select a category.");
            return;
        }

        Task task = new Task(0, name, category, minutes, date.toString(), notes, false);
        databaseManager.insertTask(task);
        loadTasksFromDatabase();

        view.getTaskNameField().clear();
        view.getPlannerCategoryBox().setValue(null);
        view.getMinutesSpinner().getValueFactory().setValue(30);
        view.getDatePicker().setValue(LocalDate.now());
        view.getNotesArea().clear();

        refreshSummary();
    }

    private void handleRemoveSelectedTask() {
        Task selected = view.getTaskTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a task to remove.");
            return;
        }

        databaseManager.deleteTask(selected.getId());
        loadTasksFromDatabase();
        refreshSummary();
    }

    private void handleToggleComplete() {
        Task selected = view.getTaskTable().getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a task to toggle.");
            return;
        }

        boolean newValue = !selected.isCompleted();
        databaseManager.updateTaskCompletion(selected.getId(), newValue);
        loadTasksFromDatabase();
        refreshSummary();
    }

    private void handleStartSession() {
        plannedMinutes.set(view.getSessionLengthBox().getValue());

        if (!timerRunning.get()) {
            if (remainingSeconds.get() <= 0 || remainingSeconds.get() > plannedMinutes.get() * 60) {
                remainingSeconds.set(plannedMinutes.get() * 60);
            }
            timeline.play();
            timerRunning.set(true);
        }
    }

    private void handlePauseSession() {
        timeline.pause();
        timerRunning.set(false);
    }

    private void handleResetSession() {
        timeline.stop();
        timerRunning.set(false);
        plannedMinutes.set(view.getSessionLengthBox().getValue());
        remainingSeconds.set(plannedMinutes.get() * 60);
        view.getTimerProgressBar().setProgress(1.0);
    }

    private void handleSaveSession() {
    int secondsCompleted = plannedMinutes.get() * 60 - remainingSeconds.get();

    if (secondsCompleted <= 10) {
        showAlert("Nothing to Save", "Let the session run a bit longer before saving.");
        return;
    }

    int minutesCompleted = Math.max(1, secondsCompleted / 60);

    SessionEntry sessionEntry = new SessionEntry(
            0,
            view.getSessionCategoryBox().getValue(),
            minutesCompleted,
            java.time.LocalTime.now().format(timeFormat)
    );

    databaseManager.insertSession(sessionEntry);
    loadSessionsFromDatabase();
    refreshSessionHistory();
    refreshSummary();

    showAlert("Session Saved", "Saved " + minutesCompleted + " minute(s).");
}
    
    private void loadDataFromDatabase() {
        loadTasksFromDatabase();
        loadSessionsFromDatabase();
    }

    private void loadTasksFromDatabase() {
        tasks.setAll(databaseManager.getAllTasks());
    }

    private void loadSessionsFromDatabase() {
        sessions.setAll(databaseManager.getAllSessions());
    }

    private void refreshSessionHistory() {
        view.getSessionListView().getItems().clear();
        for (SessionEntry sessionEntry : sessions) {
            view.getSessionListView().getItems().add(
                    sessionEntry.getStartTime() + " - " +
                    sessionEntry.getCategory() + " (" +
                    sessionEntry.getMinutes() + " min)"
            );
        }
    }

    private void refreshSummary() {
        long completedCount = tasks.stream().filter(Task::isCompleted).count();
        int totalFocusMinutes = sessions.stream().mapToInt(SessionEntry::getMinutes).sum();

        view.getTotalTasksLabel().setText("Total Tasks: " + tasks.size());
        view.getCompletedTasksLabel().setText("Completed Tasks: " + completedCount);
        view.getTotalFocusTimeLabel().setText("Total Focus Time: " + totalFocusMinutes + " minutes");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

        int study = 0;
        int work = 0;
        int personal = 0;
        int exercise = 0;
        int other = 0;

        for (SessionEntry sessionEntry : sessions) {
            switch (sessionEntry.getCategory()) {
                case "Study" -> study += sessionEntry.getMinutes();
                case "Work" -> work += sessionEntry.getMinutes();
                case "Personal" -> personal += sessionEntry.getMinutes();
                case "Exercise" -> exercise += sessionEntry.getMinutes();
                default -> other += sessionEntry.getMinutes();
            }
        }

        if (study > 0) chartData.add(new PieChart.Data("Study", study));
        if (work > 0) chartData.add(new PieChart.Data("Work", work));
        if (personal > 0) chartData.add(new PieChart.Data("Personal", personal));
        if (exercise > 0) chartData.add(new PieChart.Data("Exercise", exercise));
        if (other > 0) chartData.add(new PieChart.Data("Other", other));

        if (chartData.isEmpty()) {
            chartData.add(new PieChart.Data("No Sessions Yet", 1));
        }

        view.getSummaryChart().setData(chartData);
    }

    private String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}