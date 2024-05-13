package io.github.disbatch.command.exception;

/**
 *
 */
public class ArgumentIndexOutOfBoundsException extends CommandException {

    /**
     * @param index
     */
    public ArgumentIndexOutOfBoundsException(final int index) {
        super("Argument index out of range: " + index);
    }
}
