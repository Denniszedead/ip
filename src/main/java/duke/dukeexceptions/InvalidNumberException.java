package duke.dukeexceptions;

/**
 * An exception when the user enters an invalid number when the command requires one.
 */
public final class InvalidNumberException extends DukeExceptions {
    private static final String ERROR_MESSAGE = "You have entered an invalid number!";
    /**
     * Creates a new InvalidNumberException exception.
     */
    public InvalidNumberException() {
        super(ERROR_MESSAGE);
    }
}
