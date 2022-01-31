package duke.praser;

import duke.command.Command;
import duke.dukeexceptions.InvalidCommand;

public class Parser {
    public static Command parse(String userInput) throws InvalidCommand {
        String command;
        String parameter = "";

        // Split the parameter into command and parameter based on white space.
        String[] strings = userInput.split("\\s+");
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
