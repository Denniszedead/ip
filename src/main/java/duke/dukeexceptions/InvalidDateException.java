package duke.dukeexceptions;

/**
 * The InvalidDateException exception occurs when the users enters a date format other than dd/mm/yyyy HHmm.
 */
public final class InvalidDateException extends DukeExceptions {
    private static final String ERROR_MESSAGE = "Please enter the date for the following format: dd/mm/yyyy HHmm.";
    public InvalidDateException() {
        super(ERROR_MESSAGE);
    }
}
