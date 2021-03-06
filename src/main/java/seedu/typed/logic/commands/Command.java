package seedu.typed.logic.commands;

import seedu.typed.commons.core.Config;
import seedu.typed.commons.core.Messages;
import seedu.typed.logic.commands.exceptions.CommandException;
import seedu.typed.model.Model;
import seedu.typed.storage.Storage;
import seedu.typed.storage.temp.Session;

/**
 * Represents a command with hidden internal logic and the ability to be
 * executed.
 */
public abstract class Command {
    protected Model model;
    protected Session session;
    protected String commandText;
    protected Config config;
    protected Storage storage;

    /**
     * Constructs a feedback message to summarise an operation that displayed a
     * listing of tasks.
     *
     * @param displaySize
     *            used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException
     *             If an error occurs during command execution.
     */
    public abstract CommandResult execute() throws CommandException;

    /**
     * Provides any needed dependencies to the command. Commands making use of
     * any of these should override this method to gain access to the
     * dependencies.
     */
    public void setData(Model model, Session session, String commandText, Config config, Storage storage) {
        this.model = model;
        this.session = session;
        this.commandText = commandText;
        this.config = config;
        this.storage = storage;
    }
}
