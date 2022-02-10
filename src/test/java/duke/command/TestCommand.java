package duke.command;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import duke.dukeexceptions.DukeExceptions;
import duke.dukeexceptions.InvalidCommandException;
import duke.storage.Storage;
import duke.task.Task;
import duke.tasklist.TaskList;

public class TestCommand {
    private String fileName = "data/test/dukeEmptyTxt.txt";

    @Test
    void createCommand_noCommand_missingCommandExceptionThrown() {
        try {
            Command cmd = Command.getCommand("", "");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Sorry, I don't see any commands!", e.getMessage());
        }
    }

    @Test
    void createCommand_randomCommand_incorrectCommandExceptionThrown() {
        try {
            Command cmd = Command.getCommand("Some", "random command");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Sorry! I don't understand what are you saying!", e.getMessage());
        }
    }

    @Test
    void createByeCommand_byeCommand_byeCommand() {
        try {
            Command cmd = Command.getCommand("BYE", "");
            Assertions.assertEquals(cmd.getClass(), ByeCommand.class);
        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createListCommand_listCommandWithEmptyList_resultStringWithEmptyList() {
        Storage storage = new Storage("data/test/dukeListCmdTest1.txt");
        try {
            TaskList taskList = storage.getData();
            Command cmd = Command.getCommand("LIST", "");
            String expected = "Here you go:\n";
            Assertions.assertEquals(expected, cmd.run(taskList, storage));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createListCommand_listCommandWithTasks_resultStringWithTasks() {
        String expected = "Here you go:\n"
                + "1. [T][ ] Task 1\n"
                + "2. [D][ ] Deadline 1 " + "(by: 01 Jan 1900 12:00 AM)\n"
                + "3. [E][ ] Event 1 " + "(at: 01 Jan 1900 12:00 PM)\n"
                + "4. [T][X] Task 2\n"
                + "5. [D][X] Deadline 2 " + "(by: 01 Jan 2022 11:59 PM)\n"
                + "6. [E][X] Event 2 " + "(at: 01 Jan 2022 11:59 PM)\n";

        Storage storage = new Storage("data/test/dukeListCmdTest2.txt");

        try {
            TaskList taskList = storage.getData();
            Command cmd = Command.getCommand("LIST", "");
            String result = cmd.run(taskList, storage);
            Assertions.assertEquals(expected, result);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void createMarkCommand_markCommandWithoutNumber_emptyNumberExceptionThrown() {
        String fileName = "data/test/dukeMarkCmdTest.txt";
        Storage storage = new Storage(fileName);
        TaskList tasks = new TaskList();

        try {
            Command cmd = Command.getCommand("MARK", "");
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Mark needs a number", e.getMessage());
        }
    }

    @Test
    void createMarkCommand_markCommandWithNumber_taskMarked() {
        String initial = "TODO\n"
                + "false\n"
                + "Task 1\n"
                + "*** Next Task ***\n";
        String expectedOne = "Marked the following task to the list:\n"
                + "[T][X] Task 1"
                + "\nGood job by the way";
        String expectedTwo = "TODO\n"
                + "true\n"
                + "Task 1\n"
                + "*** Next Task ***\n";
        String fileName = "data/test/dukeMarkCmdTest.txt";

        try {
            // Initialize file data.
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(dataFile);
            FileWriter fw = new FileWriter(dataFile);

            // Add the initial data into the data file.
            tasks.addTask(Task.createTask("TODO", false, "Task 1", null));
            storage.updateData(tasks);
            String result = "";
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(initial, result);

            // Checks whether the command is created.
            Command cmd = Command.getCommand("MARK", "1");
            Assertions.assertEquals(expectedOne, cmd.run(tasks, storage));

            // Checks whether the dataFile is updated.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clear the file.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createUnmarkCommandResult_unmarkCommandWithoutNumber_emptyNumberExceptionThrown() {
        String fileName = "dukeListUnmarkCmdTest.txt";
        TaskList tasks = new TaskList();
        Storage storage = new Storage(fileName);

        try {
            Command cmd = Command.getCommand("UNMARK", "");
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Unmark needs a number", e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createUnmarkCommandResult_unmarkCommandWithNumber_taskUnmarked() {
        String initial = "TODO\n"
                + "true\n"
                + "Task 1\n"
                + "*** Next Task ***\n";
        String expectedOne = "Unmarked the following task to the list:\n"
                + "[T][ ] Task 1"
                + "\nHope you get it done soon!";
        String expectedTwo = "TODO\n"
                + "false\n"
                + "Task 1\n"
                + "*** Next Task ***\n";
        String fileName = "data/test/dukeUnmarkCmdTest.txt";

        try {
            // Initialize the files.
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(dataFile);
            FileWriter fw = new FileWriter(dataFile);

            // Initialize the data in the data file.
            tasks.addTask(Task.createTask("TODO", true, "Task 1", null));
            storage.updateData(tasks);
            String result = "";
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(initial, result);

            // Checks whether the command is created.
            Command cmd = Command.getCommand("UNMARK", "1");
            Assertions.assertEquals(expectedOne, cmd.run(tasks, storage));

            // Checks whether the dataFile is updated.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clears the dataFile.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createTodoCommand_todoWithoutName_throwsEmptyTaskException() {
        try {
            Command cmd = Command.getCommand("TODO", "");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Are you trying to add something to Todo", e.getMessage());
        }
    }

    @Test
    void createTodoCommand_todoWithName_createTodoInFile() {
        String initial = "";
        String expectedOne = "Added the following todo into the list:\n[T][ ] Task 1";
        String expectedTwo = "TODO\n"
                + "false\n"
                + "Task 1\n"
                + "*** Next Task ***\n";

        try {
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(fileName);
            FileWriter fw = new FileWriter(dataFile);

            // Checks whether the file is empty.
            String result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(initial, result);

            // Check whether the command is created
            Command cmd = Command.getCommand("TODO", "Task 1");
            result = cmd.run(tasks, storage);
            Assertions.assertEquals(expectedOne, result);

            // Checks whether data is saved in the dataFile.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clears the dataFile.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createDeadlineCommand_deadlineWithoutTaskName_throwsEmptyTaskException() {
        try {
            Command cmd = Command.getCommand("DEADLINE", "");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Are you trying to add something to Deadline", e.getMessage());
        }
    }

    @Test
    void createDeadlineCommand_deadlineWithoutDate_throwsEmptyDateException() {
        try {
            Command cmd = Command.getCommand("DEADLINE", "Deadline 1 /by");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Deadline needs a date", e.getMessage());
        }
    }

    @Test
    void createDeadlineCommand_deadlineWithInvalidDate_throwsInvalidDateException() {
        try {
            Command cmd = Command.getCommand("DEADLINE", "Deadline 1 /by 2022-01-01 1234");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Please enter the date for the following format: dd/mm/yyyy HHmm.", e.getMessage());
        }
    }

    @Test
    void createDeadlineCommand_duplicatedDate_throwsDateClashException() {
        String fileName = "data/test/dukeDuplicatedDateTest.txt";
        String date = "1/1/2022 2359";
        String expected = "There is already a deadline/event scheduled on " + date;
        try {
            Storage storage = new Storage(fileName);
            TaskList tasks = storage.getData();
            String parameter = "Deadline 2 /by " + date;
            Command cmd = Command.getCommand("DEADLINE", parameter);
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void createDeadlineCommand_correctDeadline_createDeadlineInFile() {
        String initial = "";
        String expectedOne =
                "Added the following deadline into the list:\n[D][ ] Deadline 1 (by: 01 Jan 2022 11:59 PM)";
        String expectedTwo = "DEADLINE\n"
                + "false\n"
                + "Deadline 1\n"
                + "1/1/2022 2359\n"
                + "*** Next Task ***\n";

        try {
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(fileName);
            FileWriter fw = new FileWriter(dataFile);

            // Checks whether the file is empty.
            String result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(initial, result);

            // Check whether the command is created
            Command cmd = Command.getCommand("DEADLINE", "Deadline 1 /by 01/01/2022 2359");
            result = cmd.run(tasks, storage);
            Assertions.assertEquals(expectedOne, result);

            // Checks whether data is saved in the dataFile.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clears the dataFile.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void createEventCommand_eventWithoutTaskName_throwsEmptyTaskException() {
        try {
            Command cmd = Command.getCommand("EVENT", "");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Are you trying to add something to Event", e.getMessage());
        }
    }

    @Test
    void createEventCommand_eventWithoutDate_throwsEmptyDateException() {
        try {
            Command cmd = Command.getCommand("EVENT", "Event 1 /by");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Event needs a date", e.getMessage());
        }
    }

    @Test
    void createEventCommand_deadlineWithInvalidDate_throwsInvalidDateException() {
        try {
            Command cmd = Command.getCommand("DEADLINE", "Deadline 1 /by 2022-01-01 1234");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();

            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Please enter the date for the following format: dd/mm/yyyy HHmm.", e.getMessage());
        }
    }

    @Test
    void createEventCommand_duplicatedDate_throwsDateClashException() {
        String fileName = "data/test/dukeDuplicatedDateTest.txt";
        String date = "1/1/2022 2359";
        String expected = "There is already a deadline/event scheduled on " + date;
        try {
            Storage storage = new Storage(fileName);
            TaskList tasks = storage.getData();
            String parameter = "Event 2 /at " + date;
            Command cmd = Command.getCommand("EVENT", parameter);
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void createEventCommand_correctEvent_createEventInFile() {
        String initial = "";
        String expectedOne =
                "Added the following event into the list:\n[E][ ] Event 1 (at: 03 May 2022 06:00 PM)";
        String expectedTwo = "EVENT\n"
                + "false\n"
                + "Event 1\n"
                + "3/5/2022 1800\n"
                + "*** Next Task ***\n";

        try {
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(fileName);
            FileWriter fw = new FileWriter(dataFile);

            // Checks whether the file is empty.
            String result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(initial, result);

            // Check whether the command is created
            Command cmd = Command.getCommand("EVENT", "Event 1 /at 03/05/2022 1800");
            result = cmd.run(tasks, storage);
            Assertions.assertEquals(expectedOne, result);

            // Checks whether data is saved in the dataFile.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clears the dataFile.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void deleteCommand_deleteWithoutNumber_throwEmptyNumberException() {
        String fileName = "data/test/dukeDeleteCmdTest.txt";
        Storage storage = new Storage(fileName);
        TaskList tasks = new TaskList();

        try {
            Command cmd = Command.getCommand("DELETE", "");
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("Delete needs a number", e.getMessage());
        }
    }

    @Test
    void deleteCommand_deleteWithInvalidNumber_throwInvalidNumberException() {
        String fileName = "data/test/dukeDeleteCmdTest.txt";
        Storage storage = new Storage(fileName);
        TaskList tasks = new TaskList();

        try {
            Command cmd = Command.getCommand("DELETE", "One");
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (DukeExceptions e) {
            Assertions.assertEquals("You have entered an invalid number!", e.getMessage());
        }
    }

    @Test
    void deleteCommand_correctDelete_deleteTaskInList() {
        String expectedOne = "Deleted the following task:\n[T][ ] Task 1";
        String expectedTwo = "";
        String fileName = "data/test/dukeDeleteCmdTest.txt";
        Storage storage = new Storage(fileName);
        TaskList tasks = new TaskList();
        try {
            tasks.addTask(Task.createTask("TODO", false, "Task 1", null));
            storage.updateData(tasks);
            File dataFile = new File(fileName);
            Scanner sc = new Scanner(dataFile);
            FileWriter fw = new FileWriter(fileName);

            Command cmd = Command.getCommand("DELETE", "1");
            String result = cmd.run(tasks, storage);
            Assertions.assertEquals(expectedOne, result);

            // Checks whether data is no longer in the dataFile.
            result = "";
            sc = new Scanner(dataFile);
            while (sc.hasNext()) {
                result += sc.nextLine() + "\n";
            }
            Assertions.assertEquals(expectedTwo, result);

            // Clears the dataFile.
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createFindCmd_findWithoutKeyword_throwEmptyKeywordException() {
        String fileName = "data/test/dukeFindCmdTest.txt";
        TaskList tasks = new TaskList();
        Storage storage = new Storage(fileName);

        try {
            Command cmd = Command.getCommand("FIND", "");
            String result = cmd.run(tasks, storage);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Please enter a keyword for find command!", e.getMessage());
        }
    }

    @Test
    void createFindCommand_findWithKeyword_returnTaskListWithKeyword() {
        String expected = "These are the tasks that have the keyword \"CS2103\"\n"
                + "1. [T][ ] CS2103 Task 1\n"
                + "2. [D][X] CS2103 Deadline 1 (by: 10 Feb 2022 11:59 PM)\n"
                + "3. [E][X] CS2103 Exam 1 (at: 01 Jan 2022 11:59 PM)\n";
        String fileName = "data/test/dukeFindCmdTest.txt";
        TaskList tasks = new TaskList();
        Storage storage = new Storage(fileName);

        try {
            tasks = storage.getData();

            Command cmd = Command.getCommand("FIND", "CS2103");
            String result = cmd.run(tasks, storage);
            Assertions.assertEquals(expected, result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void createHelpCommand_helpCommand_returnsStringWithHelpOfAllCommands() {
        String expected = "Here are the tasks avaliable for duke:\n"
                + "List: Shows all the tasks in duke\n"
                + "Mark <number>: Marks the task in the index <number>\n"
                + "Unmark <number>: Unmarks the task in the index <number>\n"
                + "Find <keyword>: finds all the task that contains <keyword>\n"
                + "Delete <number>: Deletes the task in index <number>\n"
                + "Todo <name of todo>: Creates a new todo <name of todo>\n"
                + "Deadline <name of deadline> <date of deadline in dd/mm/yyyy HHmm>: "
                + "Creates a new deadline <name of deadline> at <date of deadline in dd/mm/yyyy HHmm>\n"
                + "Event <name of deadline> <date of deadline in dd/mm/yyyy HHmm>: "
                + "Creates a new event <name of deadline> at <date of deadline in dd/mm/yyyy HHmm>\n"
                + "Bye: Exits duke.";
        try {
            Command cmd = Command.getCommand("HELP", "");
            Storage storage = new Storage(fileName);
            TaskList tasks = new TaskList();
            Assertions.assertEquals(expected, cmd.run(tasks, storage));
        } catch (Exception e) {
            Assertions.fail();
        }

    }
}
