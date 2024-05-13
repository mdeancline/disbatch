package io.github.disbatch.command.parameter.exception;

import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.Parameter;

/**
 * Thrown when any bounds from a {@link Parameter} cannot be used by a {@link ParameterizedCommand}.
 */
public class InvalidParameterException extends ParameterException {
    public InvalidParameterException(final String message) {
        super(message);
    }
}
