package duke.dukeexceptions;

/**
 * The List Index Out Of Bound exception is thrown when the user tries to access the index that is more than the
 * number of tasks in the task list.
 */
public final class ListIndexOutOfBoundException extends DukeExceptions {
    private static final String ERROR_MESSAGE = "Index is out of bond!";
    public ListIndexOutOfBoundException() {
        super(ERROR_MESSAGE);
    }
}
