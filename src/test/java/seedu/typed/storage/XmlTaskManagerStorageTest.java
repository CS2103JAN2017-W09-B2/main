package seedu.typed.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.typed.commons.util.FileUtil;
import seedu.typed.model.ReadOnlyTaskManager;
import seedu.typed.model.TaskManager;
import seedu.typed.model.task.TaskBuilder;
import seedu.typed.testutil.TypicalTestTasks;

public class XmlTaskManagerStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskManagerStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskManager_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readTaskManager(null);
    }

    private java.util.Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws Exception {
        return new XmlTaskManagerStorage(filePath).readTaskManager(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTaskManager("NonExistentFile.xml").isPresent());
    }

    /*
     * @Test public void read_notXmlFormat_exceptionThrown() throws Exception {
     * thrown.expect(DataConversionException.class);
     * readTaskManager("NotXmlFormatTaskManager.xml"); IMPORTANT: Any code below
     * an exception-throwing line (like the one above) will be ignored. That
     * means you should not have more than one exception test in one method
     **
     * }
     */

    @Test
    public void readAndSaveTaskManager_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempTaskManager.xml";
        TypicalTestTasks td = new TypicalTestTasks();
        TaskManager original = td.getTypicalTaskManager();
        System.out.println("Printing original : ");
        for (int i = 0; i < original.getTaskList().size(); i++) {
            System.out.println(original.getTaskList().get(i));
        }
        XmlTaskManagerStorage xmlTaskManagerStorage = new XmlTaskManagerStorage(filePath);

        // Save in new file and read back
        xmlTaskManagerStorage.saveTaskManager(original, filePath);

        ReadOnlyTaskManager readBack = xmlTaskManagerStorage.readTaskManager(filePath).get();
        System.out.println("Printing test: ");
        TaskManager toCompare = new TaskManager(readBack);

        for (int j = 0; j < toCompare.getTaskList().size(); j++) {
            System.out.println(toCompare.getTaskList().get(j));
        }
        assertEquals(original, toCompare);

        // Modify data, overwrite exiting file, and read back
        original.addTask(new TaskBuilder(td.hoon)
                .build());
        original.removeTask(new TaskBuilder(td.alice)
                .build());
        xmlTaskManagerStorage.saveTaskManager(original, filePath);
        readBack = xmlTaskManagerStorage.readTaskManager(filePath).get();
        assertEquals(original, new TaskManager(readBack));

        // Save and read without specifying file path
        original.addTask(new TaskBuilder(td.ida)
                .build());
        xmlTaskManagerStorage.saveTaskManager(original); // file path not
                                                         // specified
        readBack = xmlTaskManagerStorage.readTaskManager().get(); // file path
                                                                  // not
                                                                  // specified

        assertEquals(original, new TaskManager(readBack));

    }

    @Test
    public void saveTaskManager_nullTaskManager_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskManager(null, "SomeFile.xml");
    }

    private void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
        new XmlTaskManagerStorage(filePath).saveTaskManager(taskManager, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveTaskManager_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskManager(new TaskManager(), null);
    }

}
