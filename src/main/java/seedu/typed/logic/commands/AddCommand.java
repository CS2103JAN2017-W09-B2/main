package seedu.typed.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.typed.commons.exceptions.IllegalValueException;
import seedu.typed.commons.util.TripleUtil;
import seedu.typed.logic.commands.exceptions.CommandException;
import seedu.typed.model.tag.Tag;
import seedu.typed.model.tag.UniqueTagList;
import seedu.typed.model.task.Date;
import seedu.typed.model.task.Name;
import seedu.typed.model.task.Task;
import seedu.typed.model.task.UniqueTaskList;

/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: NAME [d/DATE]  [t/TAG]...\n" + "Example: " + COMMAND_WORD
            + " buy 5 broccolis d/tomorrow t/survival t/grocery ";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final Task toAdd;

    /**
     * Creates an AddCommand using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public AddCommand(String name, String date, Set<String> tags) throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Task(new Name(name), new Date(date), new UniqueTagList(tagSet));
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTask(toAdd);
            session.clearRedoStack();
            TripleUtil<String, Task, Task> toPush = new TripleUtil<String, Task, Task>("delete", toAdd, null);
            session.pushUndoStack(toPush);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
    }

}
