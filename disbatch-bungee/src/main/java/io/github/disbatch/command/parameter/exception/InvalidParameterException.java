package io.github.disbatch.command.parameter.exception;

import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;

/**
 * Thrown when any bounds from a {@link Parameter} cannot be used by a {@link ParameterizedCommand}.
 *
 * @since 1.0.0
 */
public class InvalidParameterException extends ParameterException {
    public InvalidParameterException(final String message) {
        super(message);
    }
}
