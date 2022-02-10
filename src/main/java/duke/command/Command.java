package duke.command;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.dukeexceptions.DateClashException;
import duke.dukeexceptions.DukeExceptions;
import duke.dukeexceptions.EmptyDateException;
import duke.dukeexceptions.EmptyKeywordException;
import duke.dukeexceptions.EmptyNumberException;
import duke.dukeexceptions.EmptyTaskException;
import duke.dukeexceptions.InvalidCommandException;
import duke.dukeexceptions.InvalidDateException;
import duke.dukeexceptions.InvalidNumberException;
import duke.dukeexceptions.ListIndexOutOfBoundException;
import duke.storage.Storage;
import duke.task.Task;
import duke.tasklist.TaskList;

/**
 * The Command object that represents the different commands of Duke.
 */
public abstract class Command {
    /** The formatter to format the user entered date. */
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected static final boolean IS_MARKED = false;
    protected String parameter;
    protected String commandName;

    /**
     * Creates a new Command.
     *
     * @param parameter The parameter entered by the user.
     */
    protected Command(String commandName, String parameter) {
        this.commandName = commandName;
        this.parameter = parameter;
    }

    /**
     * Runs the various commands based on the type of command.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The string that will be shown on the Dialogue Box.
     * @throws DukeExceptions The exception from Duke if there are errors encountered.
     */
    public abstract String run(TaskList taskList, Storage storage) throws DukeExceptions;

    /**
     * Creates the command based on the user's input in the command line.
     *
     * @param command The type of command.
     * @param parameter The parameter which varies based on the type of command.
     * @return The command with its type based on command.
     * @throws InvalidCommandException If the user enters either an invalid command or empty command.
     */
    public static Command getCommand(String command, String parameter) throws InvalidCommandException {
        if (command.equals("BYE")) {
            return new ByeCommand(parameter);
        } else if (command.equals("LIST")) {
            return new ListCommand(parameter);
        } else if (command.equals("MARK")) {
            return new MarkCommand(parameter);
        } else if (command.equals("UNMARK")) {
            return new UnmarkCommand(parameter);
        } else if (command.equals("TODO")) {
            return new TodoCommand(parameter);
        } else if (command.equals("DEADLINE")) {
            return new DeadlineCommand(parameter);
        } else if (command.equals("EVENT")) {
            return new EventCommand(parameter);
        } else if (command.equals("DELETE")) {
            return new DeleteCommand(parameter);
        } else if (command.equals("FIND")) {
            return new FindCommand(parameter);
        } else if (command.equals("HELP")) {
            return new HelpCommand(parameter);
        } else {
            // Throws invalid command as the user gives a invalid command.
            throw InvalidCommandException.createInvalidCommand(command);
        }
    }

    protected static String[] getTaskNameAndDate(int index, String parameter, String commandName)
            throws DukeExceptions {
        // Gets the task name form the parameter.
        String task = parameter.substring(0, index);
        // If the user did not enter a task name, then throw empty task exception.
        if (task.isBlank()) {
            throw EmptyTaskException.createEmptyTask(commandName);
        }

        // Get the date from the parameter.
        String date = parameter.substring(index + 5, parameter.length());
        // If the user did not enter a date after "/at".
        if (date.isBlank()) {
            throw EmptyDateException.createEmptyDate(commandName);
        }
        String[] result = {task, date};
        return result;
    }
}

/**
 * The Bye command exits the Duke application.
 */
class ByeCommand extends Command {
    /** Constructs a bye command without expecting a parameter. */
    protected ByeCommand(String parameter) {
        super("Bye", parameter);
    }

    /**
     * Causes the Duke application to exit.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return Empty string, there is no significance.
     * @throws DukeExceptions Should not have an exception unless it is an unexpected error.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        System.exit(0);

        // The code is not supposed to return any text to the dialogue box.
        return null;
    }
}

/**
 * The list command shows the task list to the user.
 */
class ListCommand extends Command {
    private static final String OPENING_SENTENCE = "Here you go:\n";
    /** Constructs the list command without expecting a parameter. */
    protected ListCommand(String parameter) {
        super("List", parameter);
    }

    /**
     * Shows all the tasks in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return All the tasks in the task list which will then be shown in the Dialogue box.
     * @throws DukeExceptions Should not have an exception unless it is an unexpected error.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        String listOfTasks = taskList.printTasks();
        String result = OPENING_SENTENCE.concat(listOfTasks);

        // The string with all the task in task list which will be displayed on the dialogue box.
        return result;
    }
}

/**
 * The mark command marks the tasks, representing the tasks as complete.
 */
class MarkCommand extends Command {
    private static final String OPENING_SENTENCE = "Marked the following task to the list:\n";
    private static final String CLOSING_SENTENCE = "\nGood job by the way";
    /** Constructs the mark command with index number of the item in the task list as the parameter. */
    protected MarkCommand(String parameter) {
        super("Mark", parameter);
    }

    /**
     * Marks the task indicated by the index in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently marked task that will be shown on the Dialogue box.
     * @throws DukeExceptions Occurs if the user did not enter a number or the number is out of range from the index.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        /* The String to be shown to the dialogue box */
        String result = null;

        // Checks if the user enters no number.
        if (parameter.isBlank()) {
            throw EmptyNumberException.createEmptyNumber(commandName);
        }

        // Tries to mark the task.
        try {
            String task = taskList.markTask(Integer.parseInt(parameter));
            result = OPENING_SENTENCE.concat(task).concat(CLOSING_SENTENCE);
        } catch (IndexOutOfBoundsException e) {
            // If the user enters a number that is out of bound of the task list.
            throw new ListIndexOutOfBoundException();
        } catch (NumberFormatException e) {
            // If the user enters a parameter that is not a number.
            throw new InvalidNumberException();
        }

        // Updates the database.
        try {
            storage.updateData(taskList);
        } catch (Exception e) {
            // Other than IndexOutOfBound and NumberFormatException, no other exception should happen.
            assert true : e.getMessage();
        }
        return result;
    }
}

/**
 * The unmarks command unmarks the task, indicating it as incomplete.
 */
class UnmarkCommand extends Command {
    protected static final String OPENING_SENTENCE = "Unmarked the following task to the list:\n";
    protected static final String CLOSING_SENTENCE = "\nHope you get it done soon!";
    /** Constructs the unmark command with index number of the item in the task list as the parameter. */
    protected UnmarkCommand(String parameter) {
        super("Unmark", parameter);
    }

    /**
     * Unmarks the task indicated by the index in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently unmarked task that will be shown in the dialogue box.
     * @throws DukeExceptions Occurs if the user did not enter a number or the number is out of range from the index.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        /* The String to be shown to the dialogue box */
        String result = null;

        // Checks if the user enters no number.
        if (parameter.isBlank()) {
            throw EmptyNumberException.createEmptyNumber(commandName);
        }

        // Tries to unmark the task.
        try {
            String task = taskList.unmarkTask(Integer.parseInt(parameter));
            result = OPENING_SENTENCE.concat(task).concat(CLOSING_SENTENCE);
        } catch (IndexOutOfBoundsException e) {
            // If the user enters a number that is out of bound of the task list.
            throw new ListIndexOutOfBoundException();
        } catch (NumberFormatException e) {
            // If the user enters a parameter that is not a number.
            throw new InvalidNumberException();
        }

        // Updates the database.
        try {
            storage.updateData(taskList);
        } catch (Exception e) {
            // Other than IndexOutOfBound and NumberFormatException, no other exception should happen.
            assert true : e.getMessage();
        }
        return result;
    }
}

/**
 * The Todo Command adds a todo tasks in the task list.
 */
class TodoCommand extends Command {
    private static final String OPENING_SENTENCE = "Added the following todo into the list:\n";

    /** Constructs the TODO command that takes in the name of the TODO task as the parameter. */
    protected TodoCommand(String parameter) {
        super("Todo", parameter);
    }

    /**
     * Adds a todo task in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently created todo which will then be shown on the dialogue box.
     * @throws DukeExceptions If the user did not indicate a name of the task.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        // Checks if the user enters an empty parameter.
        if (parameter.isBlank()) {
            throw EmptyTaskException.createEmptyTask(commandName);
        }

        boolean isMarked = false;
        LocalDateTime date = null;
        Task todo = Task.createTask(commandName, isMarked, parameter, date);
        taskList.addTask(todo);
        String todoString = todo.toString();

        // Updates the task list in the file.
        try {
            storage.updateData(taskList);
        } catch (Exception e) {
            // No exception should happen.
            assert true : e.getMessage();
        }

        // Creates the string to be shown on the dialogue box.
        String result = OPENING_SENTENCE.concat(todoString);
        return result;
    }
}

/**
 * The deadline command adds a deadline tasks.
 */
class DeadlineCommand extends Command {
    private static final String OPENING_SENTENCE = "Added the following deadline into the list:\n";
    private static final String BY = " /by ";

    /** Creates the deadline command with task name /by dd/mm/yyyy HHmm as parameter. */
    protected DeadlineCommand(String parameter) {
        super("Deadline", parameter);
    }

    /**
     * Adds a deadline command into the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently created deadline that will be then shown in the dialogue box.
     * @throws DukeExceptions If the users enters no parameter, no date or invalid date.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        // Check if the user did not enter a task name and date, then throw Empty Task parameter.
        if (parameter.isBlank()) {
            throw EmptyTaskException.createEmptyTask(commandName);
        }
        int index = parameter.indexOf(BY);
        // If the user did not enter " /by ", then throw the EmptyDateException exception as no date is entered.
        if (index < 0) {
            throw EmptyDateException.createEmptyDate(commandName);
        }

        // Get the date from the parameter.
        String[] tasksAndDates = getTaskNameAndDate(index, parameter, commandName);
        String task = tasksAndDates[0];
        String dateString = tasksAndDates[1];

        // Parse the date of the deadline as DD MMM YYYY HH:mm.
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            // If the user did not enter a valid date format.
            throw new InvalidDateException();
        }
        boolean haveDateClash = taskList.checkDateInTaskList(date);
        if (haveDateClash) {
            throw DateClashException.createDateClashException(date.format(FORMATTER));
        }
        boolean isMarked = false;
        Task deadline = Task.createTask(commandName, isMarked, task, date);

        taskList.addTask(deadline);
        String deadlineString = deadline.toString();

        // Updates the task list in the file.
        try {
            storage.updateData(taskList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates the string that is to be shown on the dialogue box.
        String result = OPENING_SENTENCE.concat(deadlineString);
        return result;
    }
}

/**
 * The event command adds an event task in the task list.
 */
class EventCommand extends Command {
    private static final String OPENING_SENTENCE = "Added the following event into the list:\n";
    private static final String AT = " /at ";
    /** Creates the event command with task name /at dd/mm/yyyy HHmm as parameter. */
    protected EventCommand(String parameter) {
        super("Event", parameter);
    }

    /**
     * Adds an event task in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently created event which will be then sent to the dialogue box.
     * @throws DukeExceptions If the users enters no parameter, no date or invalid date.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        // Check if the user did not enter a task name and date, then throw Empty Task parameter.
        if (parameter.isBlank()) {
            throw EmptyTaskException.createEmptyTask(commandName);
        }

        // Get the index "/at" from the parameter.
        int index = parameter.indexOf(AT);
        if (index < 0) {
            throw EmptyDateException.createEmptyDate(commandName);
        }

        String[] tasksAndDates = getTaskNameAndDate(index, parameter, commandName);
        String task = tasksAndDates[0];
        String dateString = tasksAndDates[1];

        // Parse the date of the deadline as DD MMM YYYY HH:mm.
        LocalDateTime date;
        try {
            // If the user did not enter a valid date format.
            date = LocalDateTime.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException();
        }
        boolean haveDateClash = taskList.checkDateInTaskList(date);
        if (haveDateClash) {
            throw DateClashException.createDateClashException(date.format(FORMATTER));
        }

        boolean isMarked = false;
        Task event = Task.createTask(commandName, isMarked, task, date);
        taskList.addTask(event);

        // Updates the task list in the file.
        try {
            storage.updateData(taskList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates the string that is to be shown on the dialogue box.
        String result = OPENING_SENTENCE.concat(event.toString());
        return result;
    }
}

/**
 * The delete command deletes a task in the task list.
 */
class DeleteCommand extends Command {
    private static final String OPENING_SENTENCE = "Deleted the following task:\n";
    /**
     * Constructs the delete command with index number of the item in the task list as the parameter.
     */
    DeleteCommand(String parameter) {
        super("Delete", parameter);
    }

    /**
     * Deletes the task indicated by the index number in the task list.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The recently deleted task that will be sent to the dialogue box.
     * @throws DukeExceptions If the user did not enter a number.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        String result = null;

        // If the user did not enter a number, returns an empty number parameter.
        if (parameter.isBlank()) {
            throw EmptyNumberException.createEmptyNumber("Delete");
        }

        // Deletes the task from the task list based on the index. Then update the file.
        try {
            String deletedTask = taskList.deleteFromIndex(Integer.parseInt(parameter));
            result = OPENING_SENTENCE.concat(deletedTask);
        } catch (IndexOutOfBoundsException e) {
            // If the user enters a number that is out of bound of the task list.
            throw new ListIndexOutOfBoundException();
        } catch (NumberFormatException e) {
            // If the user enters a parameter that is not a number.
            throw new InvalidNumberException();
        }

        // Updates the file.
        try {
            storage.updateData(taskList);
        } catch (Exception e) {
            // Other than IndexOutOfBound and NumberFormatException, no other exception should happen.
            assert true : e.getMessage();
        }
        return result;
    }
}

/**
 * Finds all the tasks in the task list that contain the keyword entered by the user.
 */
class FindCommand extends Command {
    private static final String INITIAL_SENTENCE = "These are the tasks that have the keyword \"";
    /**
     * Creates a new FindCommand object.
     *
     * @param parameter The parameter of the command entered by the user.
     */
    protected FindCommand(String parameter) {
        super("Find", parameter);
    }

    /**
     * Gets all the tasks that contains the keyword and shows it to the user.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The tasks that contain the keyword.
     * @throws DukeExceptions
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        // Check if the user did not enter a keyword, then throw the EmptyKeywordException exception.
        if (parameter.isBlank()) {
            throw new EmptyKeywordException();
        }
        String openingSentence = INITIAL_SENTENCE.concat(parameter).concat("\"\n");

        // Gets a new task list which the tasks that contains the keyword.
        TaskList filteredTaskList = taskList.findTasksFromKeyword(parameter);
        String filteredTaskListString = filteredTaskList.printTasks();
        String result = openingSentence.concat(filteredTaskListString);
        return result;
    }
}

class HelpCommand extends Command {
    /**
     * Creates a new Help Command.
     *
     * @param parameter The parameter entered by the user.
     */
    protected HelpCommand(String parameter) {
        super("Help", parameter);
    }

    /**
     * Shows all the manual for all commands in duke.
     *
     * @param taskList The TaskList which the command will act on.
     * @param storage The Storage on which the command will act on.
     * @return The string that shows the manual for all commands.
     * @throws DukeExceptions There is no Duke exception expected.
     */
    @Override
    public String run(TaskList taskList, Storage storage) throws DukeExceptions {
        String openingSentence = "Here are the tasks avaliable for duke:\n";
        String listHelp = "List: Shows all the tasks in duke\n";
        String markHelp = "Mark <number>: Marks the task in the index <number>\n";
        String unmarkHelp = "Unmark <number>: Unmarks the task in the index <number>\n";
        String findHelp = "Find <keyword>: finds all the task that contains <keyword>\n";
        String deleteHelp = "Delete <number>: Deletes the task in index <number>\n";
        String todoHelp = "Todo <name of todo>: Creates a new todo <name of todo>\n";
        String deadlineHelp = "Deadline <name of deadline> <date of deadline in dd/mm/yyyy HHmm>: "
                + "Creates a new deadline <name of deadline> at <date of deadline in dd/mm/yyyy HHmm>\n";
        String eventHelp = "Event <name of deadline> <date of deadline in dd/mm/yyyy HHmm>: "
                + "Creates a new event <name of deadline> at <date of deadline in dd/mm/yyyy HHmm>\n";
        String byeHelp = "Bye: Exits duke.";
        String result = openingSentence
                .concat(listHelp)
                .concat(markHelp)
                .concat(unmarkHelp)
                .concat(findHelp)
                .concat(deleteHelp)
                .concat(todoHelp)
                .concat(deadlineHelp)
                .concat(eventHelp)
                .concat(byeHelp);
        System.out.println(result);
        return result;
    }
}
