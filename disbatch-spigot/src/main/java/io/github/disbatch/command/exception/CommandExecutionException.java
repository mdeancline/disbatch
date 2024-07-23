package io.github.disbatch.command.exception;

/**
 * Thrown to indicate that an unexpected error occurred during the execution of a command.
 *
 * @since 1.0.0
 */
public class CommandExecutionException extends CommandException {
    private static final long serialVersionUID = -2229564495899003315L;

    /**
     * Constructs a new {@code ExecutionException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandExecutionException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandExecutionException} with the specified cause.
     *
     * @param cause the cause of the exception
     * @since 1.1.0
     */
    public CommandExecutionException(final Exception cause) {
        super(cause);
    }
}