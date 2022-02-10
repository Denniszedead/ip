package duke;

import java.io.File;
import java.io.IOException;

import duke.command.Command;
import duke.dukeexceptions.DukeExceptions;
import duke.praser.Parser;
import duke.storage.Storage;
import duke.tasklist.TaskList;

/**
 * Duke is a task list CLI application that stores three types of tasks, Todos, Deadlines and Events.
 * The tasks can be marked and unmarked to represent completeness.
 */
public class Duke {
    /** The Storage that acts as an interface between Duke and duke.txt */
    private Storage storage;
    /** The Task List that stores all the tasks */
    private TaskList taskList;

    /**
     * Constructs a new Duke application.
     *
     * @param filename The file which contains the list of tasks.
     */
    public Duke(String filename) {
        assert filename.equals("data/duke.txt") : "File data/duke.txt not found!";
        // Gets the data from filename and creates a new task list based on the data in filename.
        try {
            storage = new Storage(filename);
            taskList = storage.getData();
        } catch (IOException e) {
            // If file could not be found, create new file name and get fata from there.
            File newFile = new File("data/");
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            newFile = new File(filename);
            try {
                newFile.createNewFile();
                storage = new Storage(filename);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Causes duke to run the command.
     *
     * @param input The user input.
     * @return The string based on the command result.
     */
    public String runCommand(String input) {
        try {
            Command cmd = Parser.parse(input);
            return cmd.run(taskList, storage);
        } catch (DukeExceptions e) {
            return "Found an error:\n" + e.getMessage();
        }
    }
}
