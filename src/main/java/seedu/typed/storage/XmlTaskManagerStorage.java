package seedu.typed.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.typed.commons.core.LogsCenter;
import seedu.typed.commons.exceptions.DataConversionException;
import seedu.typed.commons.util.FileUtil;
import seedu.typed.model.ReadOnlyTaskManager;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlTaskManagerStorage implements TaskManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskManagerStorage.class);

    private String filePath;

    public XmlTaskManagerStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getTaskManagerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(filePath);
    }

    /**
     * Similar to {@link #readTaskManager()}
     *
     * @param filePath
     *            location of the data. Cannot be null
     * @throws DataConversionException
     *             if the file is not in the correct format.
     */
    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath)
            throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File taskManagerFile = new File(filePath);

        if (!taskManagerFile.exists()) {
            logger.info("TaskManager file " + taskManagerFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskManager taskManagerOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskManagerOptional);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
        saveTaskManager(taskManager, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyTaskManager)}
     *
     * @param filePath
     *            location of the data. Cannot be null
     */
    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
        assert taskManager != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskManager(taskManager));
    }

}
