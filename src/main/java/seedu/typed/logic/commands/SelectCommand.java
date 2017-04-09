package seedu.typed.logic.commands;

import seedu.typed.commons.core.EventsCenter;
import seedu.typed.commons.core.Messages;
import seedu.typed.commons.core.UnmodifiableObservableList;
import seedu.typed.commons.events.ui.JumpToListRequestEvent;
import seedu.typed.logic.commands.exceptions.CommandException;
import seedu.typed.logic.commands.util.CommandTypeUtil;
import seedu.typed.model.task.ReadOnlyTask;

/**
 * Selects a task identified using its last displayed index from the task
 * manager.
 */
public class SelectCommand extends Command {

    public final int targetIndex;

    public static final String SELECT_COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = SELECT_COMMAND_WORD
            + ": Selects the task identified by the index number used in the last task listing. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + SELECT_COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    public SelectCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
        session.updateUndoRedoStacks(CommandTypeUtil.TYPE_SELECT_TASK, -1, null);
        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));

    }
}
