package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.syntax.CommandSyntax;

/**
 * Thrown when there is an unexpected issue with a {@link CommandSyntax} parsing a command's input.
 *
 * @since 1.0.0
 */
public class ParseSyntaxException extends CommandSyntaxException {
    public ParseSyntaxException(final String message) {
        super(message);
    }

    public ParseSyntaxException(final Exception cause) {
        super(cause);
    }
}
