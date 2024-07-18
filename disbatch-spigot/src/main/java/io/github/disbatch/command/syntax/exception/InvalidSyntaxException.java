package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.syntax.CommandSyntax;

/**
 * Thrown when any bounds from a {@link CommandSyntax} cannot be used by a command.
 *
 * @since 1.0.0
 */
public class InvalidSyntaxException extends CommandSyntaxException {
    public InvalidSyntaxException(final String message) {
        super(message);
    }
}
