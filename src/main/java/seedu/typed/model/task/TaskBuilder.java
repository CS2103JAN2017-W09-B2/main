package seedu.typed.model.task;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.typed.commons.exceptions.IllegalValueException;
import seedu.typed.model.tag.Tag;
import seedu.typed.model.tag.UniqueTagList;
import seedu.typed.model.tag.UniqueTagList.DuplicateTagException;
import seedu.typed.schedule.ScheduleElement;

//@@author A0139379M
/**
 * TaskBuilder helps to build a Task object by being flexible in
 * what attributes a Task object will initialise with. In particular,
 * only a name is compulsory whereas other attributes are optional.
 * @param ReadOnlyTask An existing task to modify
 * @return Task
 * @author YIM CHIA HUI
 *
 */

public class TaskBuilder {

    private Name name;
    private Notes notes;
    private ScheduleElement se;
    private UniqueTagList tags;
    private boolean isCompleted;

    public TaskBuilder() {
        this.tags = new UniqueTagList();
        this.notes = new Notes();
    }

    public TaskBuilder(ReadOnlyTask task)
            throws IllegalValueException {
        String nameToCopy = task.getName().getValue();
        this.name = new Name(nameToCopy);
        String notesToCopy = task.getNotes().getValue();
        this.notes = new Notes(notesToCopy);
        this.se = task.getSE().getDuplicate();
        this.tags = task.getTags();
        this.isCompleted = task.getIsCompleted();
    }
    public TaskBuilder setName(String name) throws IllegalValueException {
        this.name = new Name(name);
        return this;
    }

    public TaskBuilder setName(Name name) {
        assert name != null;
        this.name = name;
        return this;
    }

    //@@author A0141094M
    public TaskBuilder setNotes(String notes) throws IllegalValueException {
        this.notes = new Notes(notes);
        return this;
    }

    public TaskBuilder setNotes(Notes notes) {
        this.notes = notes;
        return this;
    }
    //@@author

    //@@author A0139379M
    public TaskBuilder setSE(ScheduleElement se) {
        this.se = se;
        return this;
    }
    public TaskBuilder setDeadline(DateTime date) {
        this.se = new ScheduleElement(date);
        return this;
    }

    public TaskBuilder setDeadline(LocalDateTime date) {
        this.se = new ScheduleElement(new DateTime(date));
        return this;
    }

    public TaskBuilder setEvent(DateTime startDate, DateTime endDate) {
        this.se = new ScheduleElement(startDate, endDate);
        return this;
    }

    public TaskBuilder setEvent(LocalDateTime startDate, LocalDateTime endDate) {
        this.se = new ScheduleElement(new DateTime(startDate), new DateTime(endDate));
        return this;
    }

    //@@author A0141094M
    public TaskBuilder setFloating() {
        this.se = ScheduleElement.makeFloating();
        return this;
    }
    //@@author

    //@@author A0139379M
    public TaskBuilder isCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public TaskBuilder addTags(String tag) throws DuplicateTagException, IllegalValueException {
        this.tags.add(new Tag(tag));
        return this;
    }

    public TaskBuilder setTags(UniqueTagList tags) {
        this.tags = tags;
        return this;
    }

    public TaskBuilder setTags(Set<String> tags) throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.tags = new UniqueTagList(tagSet);
        return this;
    }

    public Task build() {
        return new Task(name, notes, se, tags, isCompleted);
    }

}
