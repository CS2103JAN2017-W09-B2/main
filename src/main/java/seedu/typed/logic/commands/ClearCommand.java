package seedu.typed.logic.commands;

import seedu.typed.commons.exceptions.IllegalValueException;
import seedu.typed.logic.commands.exceptions.CommandException;
import seedu.typed.logic.commands.util.CommandTypeUtil;
import seedu.typed.model.TaskManager;

/**
 * Clears the task manager.
 */
public class ClearCommand extends Command {
    //@@author A0141094M
    public static final String CLEAR_COMMAND_WORD = "clear";
    public static final String EMPTY_COMMAND_WORD = "empty";
    //@@author
    public static final String MESSAGE_SUCCESS = "Task manager has been cleared!";
    public static final String MESSAGE_FAILURE = "Task manager cannot be cleared!";

    //@@author A0143853A
    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;

        TaskManager oldTaskManager = new TaskManager();
        oldTaskManager.copyData(model.getTaskManager());
        try {
            model.resetData(new TaskManager());
        } catch (IllegalValueException e) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        session.updateUndoRedoStacks(CommandTypeUtil.TYPE_CLEAR, -1, oldTaskManager);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
