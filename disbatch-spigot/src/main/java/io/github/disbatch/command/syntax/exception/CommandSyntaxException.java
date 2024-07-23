package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.exception.CommandException;

/**
 * Thrown to indicate that there is an unexpected syntax error in the context of a command's arguments.
 *
 * @since 1.1.0
 */
public class CommandSyntaxException extends CommandException {
    private static final long serialVersionUID = -2147967784678873038L;

    /**
     * Constructs a new {@code CommandSyntaxException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandSyntaxException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandSyntaxException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public CommandSyntaxException(final Exception cause) {
        super(cause);
    }
}
