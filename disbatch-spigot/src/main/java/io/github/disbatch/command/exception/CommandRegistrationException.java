package io.github.disbatch.command.exception;

/**
 *
 */
public class CommandRegistrationException extends CommandException {

    /**
     * @param message
     */
    public CommandRegistrationException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CommandRegistrationException(final String message, final Exception cause) {
        super(cause);
    }
}
