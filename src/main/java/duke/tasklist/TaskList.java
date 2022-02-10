package duke.tasklist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import duke.task.Task;

/**
 * The task list which contains all the tasks.
 */
public class TaskList {
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /** The ArrayList which stores all the tasks. */
    private final ArrayList<Task> taskList;

    /**
     * Creates a new Task List.
     */
    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    /**
     * Marks the task located in the index provided.
     *
     * @param index The index of the task to be marked.
     * @return The string representation of the task that was recently marked.
     * @throws IndexOutOfBoundsException When the user enters the index that is outside the task list.
     */
    public String markTask(int index) throws IndexOutOfBoundsException {
        try {
            // Minus one as the index from the parameter is 1-based.
            Task task = taskList.get(index - 1);
            task.setDone();
            return task.toString();
        } catch (IndexOutOfBoundsException e) {
            // If the index is out of bound of the task list.
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Unmarks the task indicated by the index in the task list.
     *
     * @param index The index of the task to be unmarked.
     * @return The string representation of the task that was recently unmarked.
     * @throws IndexOutOfBoundsException When the user enters the index that is outside the task list.
     */
    public String unmarkTask(int index) throws IndexOutOfBoundsException {
        try {
            // Minus one as the index from the parameter is 1-based.
            Task task = taskList.get(index - 1);
            task.setUndone();
            return task.toString();
        } catch (IndexOutOfBoundsException e) {
            // If the index is out of bound of the task list.
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Adds a new task in the task list.
     *
     * @param task The new task to be added into the task list.
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Shows all the tasks in the task list.
     */
    public String printTasks() {
        String result = "";
        for (int index = 0; index < this.taskList.size(); index++) {
            result += Integer.toString(index + 1) + ". " + taskList.get(index).toString() + "\n";
        }
        return result;
    }

    /**
     * Shows the number of tasks in the task list.
     */
    public void printNoTasks() {
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    /**
     * Deletes the task indicated by the index.
     *
     * @param index The index of the task to be deleted.
     * @return The string representation of the task that was recently deleted.
     */
    public String deleteFromIndex(int index) {
        // Minus one as the index from the parameter is 1-based.
        String deletedTask = taskList.get(index - 1).toString();
        taskList.remove(index - 1);
        return deletedTask;
    }

    /**
     * Returns the string format that is stored in the file.
     *
     * @return The string representation that follows the format that is stored in the database.
     */
    public String updateDatabase() {
        String result = "";
        for (Task task: taskList) {
            result = result + task.updateIntoDatabase();
        }
        return result;
    }

    /**
     * Returns a task list with all tasks that contains the keyword.
     *
     * @param keyword The keyword to be searched.
     * @return A new task list with the tasks containing the keyword.
     */
    public TaskList findTasksFromKeyword(String keyword) {
        TaskList filteredTaskList = new TaskList();

        // Gets all the tasks that contains the keyword.
        List<Task> filteredArrayList = taskList.stream()
                .filter(task -> task.getTaskName().contains(keyword))
                .collect(Collectors.toList());

        // Puts all the tasks in the filteredArrayList into the new Task List.
        for (Task task: filteredArrayList) {
            filteredTaskList.addTask(task);
        }

        return filteredTaskList;
    }

    /**
     * Checks the task list if there is another event with the date in dateString.
     *
     * @param date The date to check whether a clash occurs.
     * @return True if there is a task with the date dateString, false otherwise.
     */
    public boolean checkDateInTaskList(LocalDateTime date) {
        boolean hasDate = taskList.stream().anyMatch(task -> task.getDate().equals(date));
        return hasDate;
    }
}
