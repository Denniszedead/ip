package duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import duke.dukeexceptions.InvalidDateException;

/**
 * The task object represents the tasks stored by duke.
 */
public abstract class Task {
    /** The date formatter on which indicates that the user will enter the date in dd/mm/yyyy HHmm. */
    protected static final DateTimeFormatter INIT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    /** The date formatter on which the date will be displayed when toString() is called. */
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    protected String taskName;
    protected boolean isMarked;
    protected LocalDateTime date = null;

    /**
     * Creates a new Task.
     *
     * @param taskName The name of the task.
     * @param done Whether the task is done or not.
     */
    protected Task(String taskName, boolean done) {
        this.taskName = taskName;
        this.isMarked = done;
    }

    /**
     * Sets the task to be complete.
     */
    public void setDone() {
        isMarked = true;
    }

    /**
     * Sets the task to be incomplete.
     */
    public void setUndone() {
        isMarked = false;
    }

    /**
     * Returns a string in the format that is stored in the file filename.
     *
     * @return The properties of the task that follows the format stored in the file filename.
     */
    public abstract String updateIntoDatabase();

    /**
     * Get the task name of the task.
     *
     * @return The name of the task.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Creates a new task.
     *
     * @param type The type of task.
     * @param done The completeness of the tasks.
     * @param name The name of the task.
     * @param date The date of the task.
     * @return A new Task object.
     * @throws InvalidDateException If the task that requires a date encounters a date in an invalid format.
     */
    public static Task createTask(String type, Boolean done, String name, LocalDateTime date)
            throws InvalidDateException {
        // Convert the task type to uppercase.
        type = type.toUpperCase();

        // Creates a new task based on the task type.
        if (type.equals("TODO")) {
            return new ToDo(name, done);
        } else if (type.equals("DEADLINE")) {
            return new Deadline(name, done, date);
        } else if (type.equals("EVENT")) {
            return new Event(name, done, date);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the date of either the deadline or event.
     *
     * @return The date of the event.
     */
    public LocalDateTime getDate() {
        return date;
    }
}

/**
 * The task representing TODO.
 */
final class ToDo extends Task {
    protected ToDo(String taskName, boolean done) {
        super(taskName, done);
    }

    /**
     * {@inheritDoc}
     *
     * @return The tasks with its properties.
     */
    @Override
    public String toString() {
        String tag = "[T]";
        String doneIndicator = "[" + (isMarked ? "X" : " ") + "]";
        return tag + doneIndicator + " " + taskName;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public String updateIntoDatabase() {
        return "TODO\n" + isMarked + "\n" + taskName + "\n" + "*** Next Task ***\n";
    }
}

/**
 * The tasks that contains the deadline.
 */
final class Deadline extends Task {
    /**
     * A task with a deadline.
     *
     * @param taskName The name of the task.
     * @param done The completeness of the task.
     * @param deadline The date representing the deadline of the task.
     * @throws InvalidDateException If the user did not enter the date in the format dd/mm/yyyy HHmm.
     */
    protected Deadline(String taskName, boolean done, LocalDateTime deadline) throws InvalidDateException {
        super(taskName, done);
        date = deadline;
    }

    /**
     * {@inheritDoc}
     *
     * @return The tasks with its properties.
     */
    @Override
    public String toString() {
        String tag = "[D]";
        String doneIndicator = "[" + (isMarked ? "X" : " ") + "]";
        String deadline = " (by: " + date.format(FORMATTER) + ")";
        return tag + doneIndicator + " " + taskName + deadline;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String updateIntoDatabase() {
        return "DEADLINE\n" + isMarked + "\n" + taskName + "\n" + date.format(INIT_FORMATTER) + "\n"
                + "*** Next Task ***\n";
    }
}

final class Event extends Task {
    /**
     * Creates the event task.
     *
     * @param taskName The name of the task.
     * @param done The completeness of the task.
     * @param eventDate
     * @throws InvalidDateException
     */
    protected Event(String taskName, boolean done, LocalDateTime eventDate) throws InvalidDateException {
        super(taskName, done);
        date = eventDate;
    }

    /**
     * {@inheritDoc}
     *
     * @return The tasks with its properties.
     */
    @Override
    public String toString() {
        String tag = "[E]";
        String doneIndicator = "[" + (isMarked ? "X" : " ") + "]";
        String eventDate = " (at: " + date.format(FORMATTER) + ")";
        return tag + doneIndicator + " " + taskName + eventDate;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String updateIntoDatabase() {
        return "EVENT\n" + isMarked + "\n" + taskName + "\n" + date.format(INIT_FORMATTER) + "\n"
                + "*** Next Task ***\n";
    }
}
