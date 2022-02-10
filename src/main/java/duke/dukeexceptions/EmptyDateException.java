package duke.dukeexceptions;

/**
 * The exception for when no date is entered for commands that requires dates.
 */
public final class EmptyDateException extends DukeExceptions {
    private EmptyDateException(String s) {
        super(s);
    }

    /**
     * Creates the Empty Date exception when no date is entered for commands that requires dates.
     *
     * @param cmd Type of command entered.
     * @return The empty date exception.
     * @throws EmptyDateException The empty date exception.
     */
    public static EmptyDateException createEmptyDate(String cmd) throws EmptyDateException {
        String closingSentence = " needs a date";
        String taskTypeFirstLetter = cmd.substring(0, 1).toUpperCase();
        String taskTypeRemainingLetters = cmd.substring(1);
        String taskType = taskTypeFirstLetter.concat(taskTypeRemainingLetters);
        String errorMessage = taskType.concat(closingSentence);
        throw new EmptyDateException(errorMessage);
    }
}
