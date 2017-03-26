package seedu.typed.logic.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import seedu.typed.commons.util.FileUtil;
import seedu.typed.logic.commands.exceptions.CommandException;

//@@author A0139392X
/*
 * If only given filename, save the file to the same directory.
 * If given a path, save the file to that path.
 */
public class SaveCommand extends Command {
    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_SUCCESS = "File saved to %1$s";

    public static final String MESSAGE_SAVE_ERROR = "Unable to save to the location.";

    public static final String MESSAGE_FILENAME_INVALID = "Filename invalid";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Saves the task manager to the location specified, or as a new name.\n"
            + "Parameters: fileName | NEW_STORAGE_LOCATION\n"
            + "Example: " + COMMAND_WORD + " Desktop/typed.xml";

    private final String fileName;

    public SaveCommand(String fileName) {
        this.fileName = fileName;
    }

    /*
     * Executes the save command.
     *
     * If the newly specified destination is valid, save to the location.
     */
    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;

        File toCopyFrom = new File(config.getTaskManagerFilePath());

        if (isAPath(this.fileName)) {
            try {
                System.out.println("This is a pathname");

                String userHomeDirectory = System.getProperty("user.home");

                System.out.println(userHomeDirectory);

                File fileToCreate = new File(userHomeDirectory + "/" + this.fileName);

                System.out.println(fileToCreate.getCanonicalPath());

                // Forms the directories if the directories are missing
                fileToCreate.getParentFile().mkdirs();

                fileToCreate.createNewFile();

                System.out.println("created file at directory: " + fileToCreate.getCanonicalPath());

                FileInputStream fis = new FileInputStream(toCopyFrom);
                FileOutputStream fos = new FileOutputStream(fileToCreate);

                int length;

                byte[] buffer = new byte[1024];

                while ((length = fis.read(buffer)) != (-1)) {
                    fos.write(buffer, 0, length);
                }

                fis.close();
                fos.close();

                return new CommandResult(String.format(MESSAGE_SUCCESS, this.fileName));
            } catch (IOException e) {
                throw new CommandException(MESSAGE_SAVE_ERROR);
            }
        } else { // input name is not directory
            if (FileUtil.isValidName(this.fileName)) {
                System.out.println("This is a fileName");
                try {
                    String currentFileDirectory = FileUtil.getFullDirectoryPath();
                    System.out.println("Directory: " + currentFileDirectory);

                    File fileToCreate = new File(currentFileDirectory + "/" + this.fileName);

                    FileUtil.transferToFile(toCopyFrom, fileToCreate);

                    return new CommandResult(String.format(MESSAGE_SUCCESS, currentFileDirectory + "/" + this.fileName));
                } catch (IOException ioe) {
                    throw new CommandException(MESSAGE_SAVE_ERROR);
                }
            } else {
                throw new CommandException(MESSAGE_FILENAME_INVALID);
            }
        }
    }

    /*
     * Returns true is the input given by the user is a path
     */
    private boolean isAPath(String fileName2) {
        return fileName2.contains("/") || fileName2.contains("\\");
    }
}
