package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.syntax.CommandSyntax;

/**
 * Thrown when there is an unexpected issue with a {@link CommandSyntax} parsing a command's arguments.
 *
 * @since 1.1.0
 */
public class CommandSyntaxParseException extends CommandSyntaxException {
    private static final long serialVersionUID = 1933941367865297562L;

    /**
     * Constructs a new {@code CommandSyntaxParseException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandSyntaxParseException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandSyntaxParseException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public CommandSyntaxParseException(final Exception cause) {
        super(cause);
    }
}
