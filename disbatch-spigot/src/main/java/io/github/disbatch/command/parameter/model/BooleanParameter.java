package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;

/**
 * Parses a {@link Boolean} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [true|false]
 *
 * @since 1.0.0
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
