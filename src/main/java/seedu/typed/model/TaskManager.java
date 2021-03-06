package seedu.typed.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.typed.commons.core.UnmodifiableObservableList;
import seedu.typed.commons.exceptions.IllegalValueException;
import seedu.typed.model.tag.Tag;
import seedu.typed.model.tag.UniqueTagList;
import seedu.typed.model.tag.UniqueTagList.DuplicateTagException;
import seedu.typed.model.task.ReadOnlyTask;
import seedu.typed.model.task.Task;
import seedu.typed.model.task.TaskBuilder;
import seedu.typed.model.task.UniqueTaskList;
import seedu.typed.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.typed.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Wraps all data at the task-manager level. Duplicates are not allowed (by
 * .equals comparison)
 */
public class TaskManager implements ReadOnlyTaskManager {
    private static final String DUPLICATE_TASK_WARNING = "Task Manager should not have duplicate tasks!";
    private static final String DUPLICATE_TAG_WARNING = "Task Manager should not have duplicate tags!";
    private final UniqueTaskList tasks;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are
     * other ways to avoid duplication among constructors.
     */
    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public TaskManager() {
    }

    /**
     * Creates a TaskManager using the Tasks and Tags in the
     * {@code toBeCopied}
     * @throws IllegalValueException
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) throws IllegalValueException {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setTasks(List<? extends ReadOnlyTask> tasks)
            throws DuplicateTaskException, IllegalValueException {
        this.tasks.setTasks(tasks);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    //@@author A0143853A
    public void copyData(ReadOnlyTaskManager newData) {
        assert newData != null;
        try {
            ObservableList<ReadOnlyTask> tasksToCopy = newData.getTaskList();
            Iterator<ReadOnlyTask> iterator = tasksToCopy.iterator();
            while (iterator.hasNext()) {
                ReadOnlyTask toCopy = iterator.next();
                addTask((Task) toCopy);
            }
        } catch (DuplicateTaskException dte) {
            assert false : "Task manager to copy from should not contain duplicate tasks.";
        }
        try {
            ObservableList<Tag> tagsToCopy = newData.getTagList();
            Iterator<Tag> iterator = tagsToCopy.iterator();
            while (iterator.hasNext()) {
                Tag toCopy = iterator.next();
                addTag(toCopy);
            }
        } catch (DuplicateTagException dte) {
            assert false : "Task manager to copy from should not contain duplicate tags.";
        }
        syncMasterTagListWith(tasks);
    }

    public void copyDataExcludingIndices(ReadOnlyTaskManager newData, int[] indicesList) {
        assert newData != null;
        assert indicesList != null;
        assert indicesList.length > 0;
        try {
            int listCount = 0;
            int iterCount = 0;
            int index = indicesList[0];

            ObservableList<ReadOnlyTask> tasksToCopy = newData.getTaskList();
            Iterator<ReadOnlyTask> iterator = tasksToCopy.iterator();
            while (iterator.hasNext()) {
                if (listCount < indicesList.length) {
                    index = indicesList[listCount];
                }
                ReadOnlyTask toCopy = iterator.next();
                if (index != iterCount) {
                    Task toAdd = new TaskBuilder(toCopy).build();
                    addTask(toAdd);
                } else {
                    listCount++;
                }
                iterCount++;
            }
        } catch (DuplicateTaskException e) {
            assert false : "Task manager to copy from should not contain duplicate tasks.";
        } catch (IllegalValueException ive) {
            assert false : "Task manager to copy from should not contain illegal task values.";
        }
        try {
            int listCount = 0;
            int iterCount = 0;
            int index = indicesList[0];

            ObservableList<Tag> tagsToCopy = newData.getTagList();
            Iterator<Tag> iterator = tagsToCopy.iterator();
            while (iterator.hasNext()) {
                if (listCount < indicesList.length) {
                    index = indicesList[listCount];
                }
                Tag toCopy = iterator.next();
                if (index != iterCount) {
                    addTag(toCopy);
                } else {
                    listCount++;
                }
                iterCount++;
            }
        } catch (DuplicateTagException e) {
            assert false : "Task manager to copy from should not contain duplicate tags.";
        }
        syncMasterTagListWith(tasks);
    }
    //@@author

    public void resetData(ReadOnlyTaskManager newData) throws IllegalValueException {
        assert newData != null;
        try {
            setTasks(newData.getTaskList());
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : DUPLICATE_TASK_WARNING;
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : DUPLICATE_TAG_WARNING;
        }
        syncMasterTagListWith(tasks);
    }

    @Override
    public void printData() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("tasks: " + i + " " + tasks.getTaskAt(i).toString());
        }
        System.out.println("Number of Deadline Tasks : " + this.getNumberDeadlines());
        System.out.println("Number of Floating Tasks : " + this.getNumberFloatingTasks());
        System.out.println("Number of Event Tasks : " + this.getNumberEvents());

        System.out.println("Number of Uncompleted Deadline : " + this.getNumberUncompletedDeadlines());
        System.out.println("Number of Uncompleted Floating : " + this.getNumberUncompletedFloatingTasks());
        System.out.println("Number of Uncompleted Event : " + this.getNumberUncompletedEvents());
        System.out.println("Number of Overdued : " + this.getNumberOverdue());
    }

    //// task-level operations

    /**
     * Adds a task to the task manager. Also checks the new task's tags and
     * updates {@link #tags} with any new tags found, and updates the Tag
     * objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException
     *             if an equivalent task already exists.
     */
    public void addTask(Task task) throws DuplicateTaskException {
        syncMasterTagListWith(task);
        tasks.add(task);
    }

    //@@author A0143853A
    public void addTask(int index, Task task) throws DuplicateTaskException {
        syncMasterTagListWith(task);
        tasks.add(index, task);
    }
    //@@author

    //@@author A0143853A
    public int getIndexOf(Task task) throws TaskNotFoundException {
        return tasks.indexOf(task);
    }
    //@@author

    //@@author A0143853A
    public Task getTaskAt(int index) {
        return tasks.getTaskAt(index);
    }
    //@@author

    /**
     * Updates the task in the list at position {@code index} with
     * {@code editedReadOnlyTask}. {@code TaskManager}'s tag list will be
     * updated with the tags of {@code editedReadOnlyTask}.
     *
     * @see #syncMasterTagListWith(Task)
     * @throws DuplicateTaskException
     *             if updating the task's details causes the task to be
     *             equivalent to another existing task in the list.
     * @throws IndexOutOfBoundsException
     *             if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(int index, ReadOnlyTask editedReadOnlyTask)
            throws DuplicateTaskException, IllegalValueException {
        assert editedReadOnlyTask != null;

        Task editedTask = new TaskBuilder(editedReadOnlyTask).build();
        tasks.updateTask(index, editedTask);
        syncMasterTagListWith(editedTask);
    }

    /**
     * Ensures that every tag in this task: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        // used for checking task tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of task tags to point to the relevant tags in the
        // master
        // tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        taskTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        task.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these tasks: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList tasks) {
        tasks.forEach(this::syncMasterTagListWith);
    }

    public boolean removeTask(ReadOnlyTask key) throws TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new TaskNotFoundException();
        }
    }

    public void removeTaskAt(int index) {
        tasks.remove(index);
    }

    //// tag-level operations

    public void addTag(Tag tag) throws UniqueTagList.DuplicateTagException {
        tags.add(tag);
    }

    //// util methods

    @Override
    public String toString() {
        return tasks.asObservableList().size() + " tasks, " + tags.asObservableList().size() + " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        return new UnmodifiableObservableList<>(tasks.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskManager // instanceof handles nulls
                        && this.tasks.equals(((TaskManager) other).tasks)
                        && this.tags.equalsOrderInsensitive(((TaskManager) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing
        // your
        // own
        return Objects.hash(tasks, tags);
    }

    public void completeTaskAt(int taskManagerIndex)
            throws DuplicateTaskException, IllegalValueException {
        tasks.completeTaskAt(taskManagerIndex);
    }


    //@@author A0143853A
    public void uncompleteTaskAt(int taskManagerIndex) throws DuplicateTaskException {
        tasks.uncompleteTaskAt(taskManagerIndex);
    }
    //@@author

    public int getNumberCompletedTasks() {
        int num = 0;
        int total = tasks.size();
        for (int i = 0; i < total; i++) {
            if (tasks.getTaskAt(i).getIsCompleted()) {
                num++;
            }
        }
        return num;
    }

    public int getNumberUncompletedTasks() {
        return tasks.size() - getNumberCompletedTasks();
    }

    public int getNumberFloatingTasks() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (tasks.getTaskAt(i).isFloating()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberUncompletedFloatingTasks() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Task task = tasks.getTaskAt(i);
            if (task.isFloating() && !task.getIsCompleted()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberEvents() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (tasks.getTaskAt(i).isEvent()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberUncompletedEvents() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Task task = tasks.getTaskAt(i);
            if (task.isEvent() && !task.getIsCompleted()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberDeadlines() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (tasks.getTaskAt(i).isDeadline()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberUncompletedDeadlines() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Task task = tasks.getTaskAt(i);
            if (task.isDeadline() && !task.getIsCompleted()) {
                count++;
            }
        }
        return count;
    }

    //@@author A0139392X
    public int getNumberOverdue() {
        int size = tasks.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Task task = tasks.getTaskAt(i);
            if (task.isOverdue() && !task.getIsCompleted()) {
                // if task is overdue and not completed
                count++;
            }
        }
        return count;
    }
    //@@author

    public void sort() {
        tasks.sort();
    }
}
