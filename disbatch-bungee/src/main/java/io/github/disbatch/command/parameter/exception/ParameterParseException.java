package io.github.disbatch.command.parameter.exception;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.Parameter;

/**
 * Thrown when there is an unexpected issue with a {@link Parameter} parsing a {@link CommandInput}.
 *
 * @since 1.0.0
 */
public class ParameterParseException extends ParameterException {
    public ParameterParseException(final String message) {
        super(message);
    }

    public ParameterParseException(final Exception cause) {
        super(cause);
    }
}
