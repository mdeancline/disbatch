package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.syntax.CommandSyntax;

/**
 * Thrown when any bounds from a {@link CommandSyntax} cannot be used by a command.
 *
 * @since 1.1.0
 */
public class CommandSyntaxBoundsException extends CommandSyntaxException {
    private static final long serialVersionUID = 7555965119474983652L;

    /**
     * Constructs a new {@code CommandSyntaxConflictException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandSyntaxBoundsException(final String message) {
        super(message);
    }
}
