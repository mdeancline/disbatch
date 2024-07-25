package io.github.disbatch.command.exception;

/**
 * Thrown to indicate that an argument index is out of bounds of a command's arguments.
 *
 * @since 1.0.0
 */
public class ArgumentIndexOutOfBoundsException extends CommandException {
    private static final long serialVersionUID = -483941089211281768L;

    /**
     * Constructs a new {@code ArgumentIndexOutOfBoundsException} with the specified index.
     *
     * @param index the index that caused the exception
     */
    public ArgumentIndexOutOfBoundsException(final int index) {
        super("Argument index out of range: " + index);
    }
}
