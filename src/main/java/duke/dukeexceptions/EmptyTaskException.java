package duke.dukeexceptions;

/**
 * The exception when no task name is entered when the command needs it.
 */
public final class EmptyTaskException extends DukeExceptions {
    private EmptyTaskException(String s) {
        super(s);
    }

    /**
     * Creates an empty task exception.
     *
     * @param cmd The type of command that did not receive the task name.
     * @return The EmptyTaskException Exception.
     * @throws EmptyTaskException The exception when there is no task name when the command needs it.
     */
    public static EmptyTaskException createEmptyTask(String cmd) throws EmptyTaskException {
        String openingStatement = "Are you trying to add something to ";
        String taskTypeFirstLetter = cmd.substring(0, 1).toUpperCase();
        String taskTypeRemainingLetters = cmd.substring(1);
        String taskType = taskTypeFirstLetter.concat(taskTypeRemainingLetters);
        String errorMessage = openingStatement.concat(taskType);
        throw new EmptyTaskException(errorMessage);
    }
}
