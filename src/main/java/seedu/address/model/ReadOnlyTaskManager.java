package seedu.address.model;


import javafx.collections.ObservableList;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyTaskManager {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
<<<<<<< HEAD:src/main/java/seedu/address/model/ReadOnlyTaskManager.java
    ObservableList<ReadOnlyTask> getTaskList();
=======
    ObservableList<ReadOnlyTask> getPersonList();
>>>>>>> parent of 9b5fb6b... test:src/main/java/seedu/address/model/ReadOnlyAddressBook.java

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
