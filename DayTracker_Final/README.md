# Day Tracker

Day Tracker is a JavaFX desktop application for planning daily tasks, running focused work sessions, and reviewing a summary of completed work. This version keeps the same three-tab layout and behavior as the original prototype while organizing the code into a Model-View-Controller structure and storing tasks and sessions in SQLite.

## Features

- Add daily tasks with category, estimated minutes, date, and notes
- View tasks in a table with completion status
- Remove selected tasks
- Toggle task completion
- Run a focus timer with selectable length and category
- Save completed focus sessions to SQLite
- Refresh and view session history
- Review task totals, completed totals, focus totals, and a category chart

## MVC Structure

- `Main.java` starts the program
- `DayTrackerView.java` builds the JavaFX interface
- `DayTrackerController.java` handles events and app logic
- `Task.java` and `SessionEntry.java` are model classes
- `DatabaseManager.java` handles SQLite database operations

## Requirements

- Java 17 or later
- JavaFX SDK
- SQLite JDBC jar

## How to Clone and Run

1. Clone the repository:
   `git clone <your-repository-url>`

2. Open the project in jGRASP.

3. Keep all `.java` files inside the `src` folder.

4. Make sure the SQLite JDBC jar is added to the jGRASP workspace classpath.
   Example:
   `C:\Program Files (x86)\jGRASP\sqlite-jdbc-3.51.3.0.jar`

5. Use these compiler and run arguments:
   `--module-path "C:\Program Files (x86)\jGRASP\javafx-sdk-26\lib" --add-modules javafx.controls,javafx.fxml`

6. Compile all `.java` files in `src`.

7. Run `Main.java`.

## What Works in the Final Version

- Planner tab for adding, viewing, removing, and completing tasks
- Focus Session tab with timer, reset, pause, and session history
- Summary tab with totals and pie chart
- SQLite persistence for tasks and sessions
- Automatic database creation on first run

## Known Limitations

- The project depends on the SQLite JDBC jar being present in the jGRASP classpath
- UML is provided as an image and a simple source diagram text file rather than generated automatically in the IDE
- No cloud sync or multi-user support

## Demo Data

The application can be demonstrated by adding tasks and running sessions during the presentation. The database file `daytracker.db` is created automatically on first run.
