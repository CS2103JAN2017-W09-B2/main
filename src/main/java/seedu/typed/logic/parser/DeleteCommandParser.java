package seedu.typed.logic.parser;

import static seedu.typed.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.typed.logic.commands.Command;
import seedu.typed.logic.commands.DeleteCommand;
import seedu.typed.logic.commands.IncorrectCommand;
import seedu.typed.logic.commands.util.IndexRangeUtil;
//@@author A0143853A
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteCommand and returns an DeleteCommand object for execution.
     */
    public Command parse(String args) {

        /* Optional<Integer> index = ParserUtil.parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());*/

        String trimmedArgs = args.trim();

        if (trimmedArgs.equals("all")) {
            return new DeleteCommand();
        }

        IndexRangeUtil range = new IndexRangeUtil(args);
        if (range.isValid()) {
            return new DeleteCommand(range.getStartIndex(),
                    range.getEndIndex());
        } else {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteCommand.MESSAGE_USAGE));
        }
    }

}
