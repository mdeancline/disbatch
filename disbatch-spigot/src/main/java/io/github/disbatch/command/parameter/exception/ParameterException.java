package io.github.disbatch.command.parameter.exception;

import io.github.disbatch.command.exception.CommandException;

/**
 *
 */
public class ParameterException extends CommandException {
    public ParameterException(final String message) {
        super(message);
    }
    
    public ParameterException(final Exception cause) {
        super(cause);
    }
}
