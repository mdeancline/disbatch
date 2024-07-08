package io.github.disbatch.command.exception;

/**
 * @since 1.0.0
 */
public class CommandException extends RuntimeException {
    public CommandException(final String message) {
        super(message);
    }

    public CommandException(final Exception cause) {
        super(cause);
    }
}
