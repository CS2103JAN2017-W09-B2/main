package seedu.typed.model;

import javafx.collections.ObservableList;
import seedu.typed.model.tag.Tag;
import seedu.typed.model.task.ReadOnlyTask;

/**
 * Unmodifiable view of a task manager
 */
public interface ReadOnlyTaskManager {

    /**
     * Returns an unmodifiable view of the tasks list. This list will not
     * contain any duplicate tasks.
     */
    ObservableList<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of the tags list. This list will not contain
     * any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    public void printData();


}
