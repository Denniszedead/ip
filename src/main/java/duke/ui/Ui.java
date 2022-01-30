package duke.ui;

import duke.command.Command;
import duke.dukeexceptions.DukeExceptions;
import duke.dukeexceptions.InvalidCommand;
import duke.praser.Praser;
import duke.task.Task;
import duke.tasklist.TaskList;
import duke.uicomponents.*;

/**
 * The UI controls the user interactions.
 */
public class Ui {
    /** The menu that is shown when duke starts. */
    Menu menu;
    /** The exit screen when duke exits */
    ExitScreen exitScreen;
    /** The error menu when there are error detected */
    ErrorMenu errorMenu;
    /** The screen that shows the task list. */
    public TaskListDisplay taskListDisplay;
    /** The screen that shows the task that is recently marked */
    MarkTaskDisplay markTaskDisplay;
    /** The screen that shows the task that is recently unmarked */
    UnmarkedTaskDisplay unmarkedTaskDisplay;
    /** The screen that shows the task when a new task is added */
    NewTaskDisplay newTaskDisplay;
    /** The screen that shows the task when a task is deleted */
    DeleteTaskDisplay deleteTaskDisplay;

    /**
     * Creates a new UI for duke.
     */
    public Ui() {
        this.menu = new Menu();
        this.exitScreen = new ExitScreen();
        this.taskListDisplay = new TaskListDisplay();
        this.markTaskDisplay = new MarkTaskDisplay();
        this.unmarkedTaskDisplay = new UnmarkedTaskDisplay();
        this.newTaskDisplay = new NewTaskDisplay();
        this.deleteTaskDisplay = new DeleteTaskDisplay();
        this.errorMenu = new ErrorMenu();
    }

    /**
     * Shows the menu when duke starts.
     */
    public void showMenu() {
        menu.run();
    }

    /**
     * Facilitates the user inputs.
     *
     * @param userInput The user input.
     * @return A command that is entered by the user.
     * @throws InvalidCommand If the user input is either empty or the user enters an invalid command.
     */
    public Command showUserCommandLine(String userInput) throws InvalidCommand {
        return Praser.prase(userInput);
    }

    /**
     * Shows the exception message if an exception is encountered.
     *
     * @param exceptions The exception to be displayed.
     */
    public void showCommandError(DukeExceptions exceptions) {
        this.errorMenu.run(exceptions.getMessage());
    }

    /**
     * Shows the exit screen when the user exits duke.
     */
    public void showExitScreen() {
        this.exitScreen.run();
    }

    /**
     * Shows all the tasks in the task list.
     *
     * @param taskList The task list to be shown.
     */
    void showTaskListDisplay(TaskList taskList) {
        this.taskListDisplay.run(taskList);
    }

    /**
     * Shows the tasts that was recently marked.
     *
     * @param task The task that was marked.
     */
    public void showMarkedTaskDisplay(String task) {
        this.markTaskDisplay.run(task);
    }

    /**
     * Shows the tasks that was recently unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarkedTaskDisplay(String task) {
        this.unmarkedTaskDisplay.run(task);
    }

    /**
     * Shows the task that was recently added into task list.
     *
     * @param task The task that was recently added.
     * @param taskList The task list which will then show the number of tasks now.
     */
    public void newTaskDisplay(Task task, TaskList taskList) {
        this.newTaskDisplay.run(task, taskList);
    }

    /**
     * Shows the task that was recently deleted.
     *
     * @param task The task that was recently deleted.
     * @param taskList The task list which will show the number of tasks left.
     */
    public void showDeleteTaskDisplay(String task, TaskList taskList) {
        this.deleteTaskDisplay.run(task, taskList);
    }
}
