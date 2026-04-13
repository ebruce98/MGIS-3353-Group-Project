import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;

/**
 * Builds the JavaFX user interface for the Day Tracker application.
 */
public class DayTrackerView {

    private final BorderPane root = new BorderPane();

    private final Label todayDateLabel = new Label();
    private final Label timerLabel = new Label();
    private final ProgressBar timerProgressBar = new ProgressBar(1.0);

    private final Label totalTasksLabel = new Label();
    private final Label completedTasksLabel = new Label();
    private final Label totalFocusTimeLabel = new Label();

    private final PieChart summaryChart = new PieChart();
    private final ListView<String> sessionListView = new ListView<>();

    private final TextField taskNameField = new TextField();
    private final ComboBox<String> plannerCategoryBox = new ComboBox<>();
    private final Spinner<Integer> minutesSpinner = new Spinner<>(5, 600, 30, 5);
    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final TextArea notesArea = new TextArea();

    private final Button addTaskButton = new Button("Add Task");
    private final Button removeSelectedButton = new Button("Remove Selected Task");
    private final Button markDoneButton = new Button("Toggle Complete");

    private final TableView<Task> taskTable = new TableView<>();
    private final TableColumn<Task, Boolean> doneCol = new TableColumn<>("Done");
    private final TableColumn<Task, String> nameCol = new TableColumn<>("Task");
    private final TableColumn<Task, String> categoryCol = new TableColumn<>("Category");
    private final TableColumn<Task, Integer> minutesCol = new TableColumn<>("Minutes");
    private final TableColumn<Task, String> dateCol = new TableColumn<>("Date");
    private final TableColumn<Task, String> notesCol = new TableColumn<>("Notes");

    private final ComboBox<Integer> sessionLengthBox = new ComboBox<>();
    private final ComboBox<String> sessionCategoryBox = new ComboBox<>();
    private final Button startButton = new Button("Start Session");
    private final Button pauseButton = new Button("Pause");
    private final Button resetButton = new Button("Reset");
    private final Button saveSessionButton = new Button("Save Session");
    private final Button refreshSessionViewButton = new Button("Refresh Session History");
    private final Button refreshSummaryButton = new Button("Refresh Summary");

    /**
     * Creates the full user interface.
     */
    public DayTrackerView() {
        root.setPadding(new Insets(15));
        root.setTop(buildHeader());
        root.setCenter(buildTabs());
        configureControls();
    }

    /**
     * Returns the application root.
     *
     * @return root node
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Binds the timer label to a formatted value.
     *
     * @param value timer text binding
     */
    public void bindTimerLabel(ObservableValue<String> value) {
        timerLabel.textProperty().bind(value);
    }

    public Label getTodayDateLabel() {
        return todayDateLabel;
    }

    public ProgressBar getTimerProgressBar() {
        return timerProgressBar;
    }

    public Label getTotalTasksLabel() {
        return totalTasksLabel;
    }

    public Label getCompletedTasksLabel() {
        return completedTasksLabel;
    }

    public Label getTotalFocusTimeLabel() {
        return totalFocusTimeLabel;
    }

    public PieChart getSummaryChart() {
        return summaryChart;
    }

    public ListView<String> getSessionListView() {
        return sessionListView;
    }

    public TextField getTaskNameField() {
        return taskNameField;
    }

    public ComboBox<String> getPlannerCategoryBox() {
        return plannerCategoryBox;
    }

    public Spinner<Integer> getMinutesSpinner() {
        return minutesSpinner;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public TextArea getNotesArea() {
        return notesArea;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public Button getRemoveSelectedButton() {
        return removeSelectedButton;
    }

    public Button getMarkDoneButton() {
        return markDoneButton;
    }

    public TableView<Task> getTaskTable() {
        return taskTable;
    }

    public ComboBox<Integer> getSessionLengthBox() {
        return sessionLengthBox;
    }

    public ComboBox<String> getSessionCategoryBox() {
        return sessionCategoryBox;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getSaveSessionButton() {
        return saveSessionButton;
    }

    public Button getRefreshSessionViewButton() {
        return refreshSessionViewButton;
    }

    public Button getRefreshSummaryButton() {
        return refreshSummaryButton;
    }

    private VBox buildHeader() {
        Label title = new Label("Day Tracker");
        title.setFont(Font.font(28));

        Label subtitle = new Label("Plan tasks, track focused work, and review your day.");
        subtitle.setStyle("-fx-text-fill: #555555; -fx-font-size: 14px;");

        todayDateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

        VBox header = new VBox(5, title, subtitle, todayDateLabel);
        header.setPadding(new Insets(0, 0, 15, 0));
        return header;
    }

    private TabPane buildTabs() {
        TabPane tabs = new TabPane();

        Tab plannerTab = new Tab("Daily Planner", buildPlannerTab());
        Tab focusTab = new Tab("Focus Session", buildFocusTab());
        Tab summaryTab = new Tab("Summary", buildSummaryTab());

        plannerTab.setClosable(false);
        focusTab.setClosable(false);
        summaryTab.setClosable(false);

        tabs.getTabs().addAll(plannerTab, focusTab, summaryTab);
        return tabs;
    }

    private BorderPane buildPlannerTab() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        formBox.setPrefWidth(300);
        formBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label formTitle = new Label("Add Daily Task");
        formTitle.setFont(Font.font(18));

        taskNameField.setPromptText("Task name");
        plannerCategoryBox.setPromptText("Category");
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(4);
        addTaskButton.setMaxWidth(Double.MAX_VALUE);

        formBox.getChildren().addAll(
                formTitle,
                new Label("Task Name"),
                taskNameField,
                new Label("Category"),
                plannerCategoryBox,
                new Label("Estimated Minutes"),
                minutesSpinner,
                new Label("Date"),
                datePicker,
                new Label("Notes"),
                notesArea,
                addTaskButton
        );

        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        taskTable.getColumns().addAll(doneCol, nameCol, categoryCol, minutesCol, dateCol, notesCol);

        HBox buttonRow = new HBox(10, markDoneButton, removeSelectedButton);
        buttonRow.setPadding(new Insets(10, 0, 0, 0));

        VBox centerBox = new VBox(10, taskTable, buttonRow);
        centerBox.setPadding(new Insets(0, 0, 0, 15));
        VBox.setVgrow(taskTable, Priority.ALWAYS);

        pane.setLeft(formBox);
        pane.setCenter(centerBox);
        return pane;
    }

    private VBox buildFocusTab() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(15));
        pane.setAlignment(Pos.TOP_CENTER);

        Label sectionTitle = new Label("Focused Work Session");
        sectionTitle.setFont(Font.font(22));

        timerLabel.setFont(Font.font(42));
        timerProgressBar.setPrefWidth(350);

        HBox controls = new HBox(10, startButton, pauseButton, resetButton, saveSessionButton);
        controls.setAlignment(Pos.CENTER);

        GridPane settingsGrid = new GridPane();
        settingsGrid.setHgap(10);
        settingsGrid.setVgap(10);
        settingsGrid.setAlignment(Pos.CENTER);
        settingsGrid.add(new Label("Session Length (min):"), 0, 0);
        settingsGrid.add(sessionLengthBox, 1, 0);
        settingsGrid.add(new Label("Session Category:"), 0, 1);
        settingsGrid.add(sessionCategoryBox, 1, 1);

        sessionListView.setPrefHeight(220);

        pane.getChildren().addAll(
                sectionTitle,
                settingsGrid,
                timerLabel,
                timerProgressBar,
                controls,
                new Label("Completed Sessions Today"),
                sessionListView,
                refreshSessionViewButton
        );

        return pane;
    }

    private VBox buildSummaryTab() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(15));

        Label title = new Label("Daily Summary");
        title.setFont(Font.font(22));

        VBox statsBox = new VBox(10, totalTasksLabel, completedTasksLabel, totalFocusTimeLabel);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 6; -fx-background-radius: 6;");

        summaryChart.setTitle("Time Spent by Session Category");
        summaryChart.setLabelsVisible(true);

        pane.getChildren().addAll(title, statsBox, summaryChart, refreshSummaryButton);
        VBox.setVgrow(summaryChart, Priority.ALWAYS);
        return pane;
    }

    private void configureControls() {
        plannerCategoryBox.getItems().addAll("Study", "Work", "Personal", "Exercise", "Other");
        sessionCategoryBox.getItems().addAll("Study", "Work", "Personal", "Exercise", "Other");
        sessionLengthBox.getItems().addAll(15, 25, 30, 45, 60);

        minutesSpinner.setEditable(true);
        sessionCategoryBox.setValue("Study");
        sessionLengthBox.setValue(25);

        doneCol.setCellValueFactory(cellData -> cellData.getValue().completedProperty());
        doneCol.setCellFactory(CheckBoxTableCell.forTableColumn(doneCol));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        minutesCol.setCellValueFactory(new PropertyValueFactory<>("minutes"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }
}