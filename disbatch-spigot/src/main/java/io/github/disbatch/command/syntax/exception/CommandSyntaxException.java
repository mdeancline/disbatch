package io.github.disbatch.command.syntax.exception;

import io.github.disbatch.command.exception.CommandException;

/**
 * @since 1.0.0
 */
public class CommandSyntaxException extends CommandException {
    public CommandSyntaxException(final String message) {
        super(message);
    }
    
    public CommandSyntaxException(final Exception cause) {
        super(cause);
    }
}
