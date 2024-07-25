package io.github.disbatch.command.exception;

/**
 * The base class for all command-related exceptions.
 *
 * @apiNote Not to be confused with {@link org.bukkit.command.CommandException}.
 * @since 1.0.0
 */
public class CommandException extends RuntimeException {
    private static final long serialVersionUID = -7243163717192061301L;

    /**
     * Constructs a new {@code CommandException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public CommandException(final Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code CommandException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public CommandException(final String message, final Exception cause) {
        super(message, cause);
    }
}
