package duke.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import duke.dukeexceptions.DukeExceptions;
import duke.task.Task;
import duke.tasklist.TaskList;

/**
 * The Storage object acts as an interface between Duke and the filename for database management.
 */
public class Storage {
    /** The formatter to format the user entered date. */
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final String NEXT_ITEM_LINE = "*** Next Task ***";
    /** The file which stores the tasks in the task list. */
    private File file;

    /**
     * Creates a new Storage object with the filename to store and retrieves the data in the tasks list.
     *
     * @param filename THe file where the tasks in the tasks list is retrieved and stored.
     */
    public Storage(String filename) {
        assert filename != null : "Filename is empty";
        this.file = new File(filename);
    }

    /**
     * Gets all the tasks in file and creates a new task list based on the data retrieved.
     *
     * @return Task List with all the tasks retrieved from the file filename.
     * @throws FileNotFoundException If the file filename could not be found in duke.
     */
    public TaskList getData() throws FileNotFoundException {
        Scanner fileInput = new Scanner(file);
        TaskList taskList = new TaskList();

        // Gets the data from the file and creates new task into the task list.
        while (fileInput.hasNextLine()) {
            // Gets the properties for a task.
            String type = fileInput.nextLine();
            Boolean isDone = Boolean.parseBoolean(fileInput.nextLine());
            String name = fileInput.nextLine();
            String dateString = fileInput.nextLine();
            LocalDateTime date = null;
            // If the program finishes processing a task.
            if (dateString.equals(NEXT_ITEM_LINE)) {
                date = null;
            } else {
                // Changes the date to the LocalDateFormat
                date = LocalDateTime.parse(dateString, FORMATTER);
                fileInput.nextLine();
            }

            // Adds the task to the task list.
            try {
                taskList.addTask(Task.createTask(type, isDone, name, date));
            } catch (DukeExceptions e) {
                System.out.println(e.getMessage());
            }
        }
        return taskList;
    }

    /**
     * Updates the file filename with the latest data from the task list.
     *
     * @param taskList The task list which has the latest data.
     * @throws IOException If an IO error happens.
     */
    public void updateData(TaskList taskList) throws IOException {
        // Creates a new filewriter.
        FileWriter fw = new FileWriter(file);

        // Writes all the tasks in the task list into the file.
        fw.write(taskList.updateDatabase());

        // Closes the filewriter.
        fw.close();
    }
}
