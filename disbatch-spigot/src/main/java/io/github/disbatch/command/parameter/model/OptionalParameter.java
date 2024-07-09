package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.exception.ParameterParseException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 *
 * @since 1.1.0
 */
public final class OptionalParameter<S extends CommandSender, V> implements Parameter<S, V> {
    private final Parameter<S, V> innerParameter;

    public OptionalParameter(final @NotNull Parameter<S, V> innerParameter) {
        this.innerParameter = innerParameter;
    }

    @Override
    public @Nullable V parse(final S sender, final CommandInput input) {
        if (input.getArgumentLength() > 0)
            return innerParameter.parse(sender, input);

        throw new ParameterParseException("OptionalParameter was forced to parse the input");
    }

    @Override
    public int getMinimumUsage() {
        return 0;
    }

    @Override
    public int getMaximumUsage() {
        return innerParameter.getMaximumUsage();
    }
}
