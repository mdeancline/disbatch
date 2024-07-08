package io.github.disbatch.command.parameter.exception;

import io.github.disbatch.command.exception.CommandException;

/**
 * @since 1.0.0
 */
public class ParameterException extends CommandException {
    public ParameterException(final String message) {
        super(message);
    }
    
    public ParameterException(final Exception cause) {
        super(cause);
    }
}
