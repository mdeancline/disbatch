package io.github.disbatch.command.exception;

/**
 * Thrown to indicate that an error occurred during the registration of a command.
 *
 * @since 1.0.0
 */
public class CommandRegistrationException extends CommandException {

    private static final long serialVersionUID = -6227075006124308494L;

    /**
     * Constructs a new {@code CommandRegistrationException} with the specified detail message.
     *
     * @param message the detail message
     */
    public CommandRegistrationException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandRegistrationException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public CommandRegistrationException(final String message, final Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code CommandRegistrationException} with the specified cause.
     *
     * @param cause the cause of the exception
     * @since 1.1.0
     */
    public CommandRegistrationException(final Exception cause) {
        super(cause);
    }
}
