package duke.dukeexceptions;

/**
 * The exception when the command entered by the user is not valid.
 */
public abstract class InvalidCommandException extends DukeExceptions {
    protected InvalidCommandException(String s) {
        super(s);
    }

    /**
     * Creates an invalid command when the user enters one.
     *
     * @param cmd The user input. Can be Empty.
     * @return An Invalid command.
     * @throws InvalidCommandException Either MissingCommandException Exception representing no command is entered or
     *                                 IncorrectCommand representing commands other than specified in the Command class
     *                                 is entered.
     */
    public static InvalidCommandException createInvalidCommand(String cmd) throws InvalidCommandException {
        throw cmd.isBlank() ? new MissingCommandException() : new IncorrectCommandException();
    }
}

/**
 * The missing command exception that represents the user enters an empty command.
 */
final class MissingCommandException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "Sorry, I don't see any commands!";
    MissingCommandException() {
        super(ERROR_MESSAGE);
    }
}

/**
 * The incorrect command exception that represents the users enters a command that is not specified by Duke.
 */
final class IncorrectCommandException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "Sorry! I don't understand what are you saying!";
    IncorrectCommandException() {
        super(ERROR_MESSAGE);
    }
}
