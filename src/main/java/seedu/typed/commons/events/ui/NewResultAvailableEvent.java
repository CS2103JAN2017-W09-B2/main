package seedu.typed.commons.events.ui;

import seedu.typed.commons.events.BaseEvent;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;

    public NewResultAvailableEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
