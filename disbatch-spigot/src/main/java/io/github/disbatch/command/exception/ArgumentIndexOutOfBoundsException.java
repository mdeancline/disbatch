package io.github.disbatch.command.exception;

/**
 * @since 1.0.0
 */
public class ArgumentIndexOutOfBoundsException extends CommandException {

    /**
     * @param index
     */
    public ArgumentIndexOutOfBoundsException(final int index) {
        super("Argument index out of range: " + index);
    }
}
