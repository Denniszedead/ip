package duke.dukeexceptions;

/**
 * Thrown when the user did not enter the keyword when the command requires one.
 */
public class EmptyKeywordException extends DukeExceptions {
    private static final String ERROR_MESSAGE = "Please enter a keyword for find command!";

    /**
     * Creates a new EmptyKeywordException exception.
     */
    public EmptyKeywordException() {
        super(ERROR_MESSAGE);
    }
}
