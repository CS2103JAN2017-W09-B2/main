package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 *
 */
public class TypicalTestTasks {

    public TestTask alice, benson, carl;

    public TypicalTestTasks() {
        try {
            alice = new TaskBuilder().withName("Have Fun All DAY ALL NIGHT")
                    .withDate("85355255")
                    .withTags("friends").build();
            benson = new TaskBuilder().withName("Party all NIGHT")
            		.withDate("98765432")
                    .withTags("owesMoney", "friends").build();
            carl = new TaskBuilder()
            		.withName("Thinking about life")
            		.withDate("95352563").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadTaskManagerWithSampleData(TaskManager ab) {
        for (TestTask task : new TypicalTestTasks().getTypicalTasks()) {
            try {
                ab.addTask(new Task(task));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{alice, benson, carl};
    }

    public TaskManager getTypicalTaskManager() {
        TaskManager tm = new TaskManager();
        loadTaskManagerWithSampleData(tm);
        return tm;
    }
}
