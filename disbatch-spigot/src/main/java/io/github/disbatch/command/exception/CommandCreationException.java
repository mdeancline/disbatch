package io.github.disbatch.command.exception;

import io.github.disbatch.Command;

/**
 * Thrown when there is an error during the creation of a {@link Command}.
 *
 * @since 1.1.0
 */
public class CommandCreationException extends CommandException {

    /**
     * Constructs a new CommandCreationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CommandCreationException(final String message) {
        super(message);
    }
}
