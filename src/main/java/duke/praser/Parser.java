package duke.praser;

import duke.command.Command;
import duke.dukeexceptions.InvalidCommandException;

/**
 * The parser object make sures that the command entered by the user is correct.
 */
public class Parser {
    /**
     * Prase the user input and returns the corresponding command.
     *
     * @param userInput The input entered by the user.
     * @return The Command as typed by the user.
     * @throws InvalidCommandException If the user inputs an invalid command.
     */
    public static Command parse(String userInput) throws InvalidCommandException {
        String command;
        String parameter = "";

        // Split the parameter into command and parameter based on white space.
        String whitespaceWord = "\\s+";
        String[] strings = userInput.split(whitespaceWord);
        if (strings.length < 2) {
            // If there are no words after space.
            command = userInput;
        } else {
            command = strings[0];
            parameter = userInput.substring(command.length() + 1);
        }

        // Returns the command based on the command and parameter.
        return Command.getCommand(command.toUpperCase(), parameter);
    }
}
