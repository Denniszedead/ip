package duke.dukeexceptions;

/**
 * An exception happens when there is another task with the same date in the task list.
 */
public class DateClashException extends DukeExceptions {
    private DateClashException(String s) {
        super(s);
    }

    /**
     * Creates a new date clash exception.
     *
     * @param date The date of which there is a clash.
     * @return An new Date Clash Exception.
     * @throws DateClashException When there is a clash of date during task creation.
     */
    public static DateClashException createDateClashException(String date) throws DateClashException {
        String openingSentence = "There is already a deadline/event scheduled on ";
        String result = openingSentence.concat(date);
        throw new DateClashException(result);
    }
}
