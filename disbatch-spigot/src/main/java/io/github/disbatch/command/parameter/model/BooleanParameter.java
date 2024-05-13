package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;

import java.util.UUID;

/**
 * Parses a {@link Boolean} based on a parsable, passed argument.
 */
public final class BooleanParameter extends SenderIndependentParameter<Boolean> {
    
    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }

    @Override
    protected Boolean parse(final CommandInput input) {
        final String argument = input.getArgument(0);

        return Boolean.TRUE.toString().equals(argument) || Boolean.FALSE.toString().equals(argument)
                ? Boolean.valueOf(argument)
                : null;
    }
}
