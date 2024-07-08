package io.github.disbatch.command.parameter.decorator;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.exception.ParameterException;
import io.github.disbatch.command.parameter.exception.ParameterParseException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * Serves as a way of switching to any {@link Parameter} at any time. This is especially useful if you want
 * different {@code Object} creation behaviors to reflect various settings during the server's runtime, for instance.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class MutableParameter<S extends CommandSender, V> implements Parameter<S, V> {
    private static final Parameter<?, ?> EMPTY = new EmptyParameter();

    @SuppressWarnings("unchecked")
    private Parameter<S, V> underlyingParameter = (Parameter<S, V>) EMPTY;

    /**
     * @param underlyingParameter
     */
    public void setUnderlyingParameter(final @NotNull Parameter<S, V> underlyingParameter) {
        this.underlyingParameter = underlyingParameter;
    }

    @Override
    public @Nullable V parse(final S sender, final CommandInput input) {
        return underlyingParameter.parse(sender, input);
    }

    @Override
    public Collection<String> tabComplete(final S sender, final CommandInput input) {
        return underlyingParameter.getSuggestions(sender, input);
    }

    @Override
    public int getMinimumUsage() {
        return underlyingParameter.getMinimumUsage();
    }

    @Override
    public int getMaximumUsage() {
        return underlyingParameter.getMaximumUsage();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("underlyingParameter=" + underlyingParameter)
                .toString();
    }

    private static class EmptyParameter implements Parameter<CommandSender, Object> {
        @Override
        public @Nullable Void parse(final CommandSender sender, final CommandInput input) {
            throw newParameterException();
        }

        private ParameterException newParameterException() {
            return new ParameterParseException("Cannot parse an empty parameter");
        }

        @Override
        public Collection<String> tabComplete(final CommandSender sender, final CommandInput input) {
            return ImmutableList.of();
        }

        @Override
        public int getMinimumUsage() {
            return 1;
        }

        @Override
        public int getMaximumUsage() {
            return 1;
        }
    }
}
