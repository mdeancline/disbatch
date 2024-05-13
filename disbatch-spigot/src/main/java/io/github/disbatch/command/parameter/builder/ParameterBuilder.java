package io.github.disbatch.command.parameter.builder;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.Parameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * @param <S>
 * @param <V>
 * @see ParameterBuilder#of(ParameterizedCommand)
 */
@ApiStatus.AvailableSince("1.0")
public final class ParameterBuilder<S extends CommandSender, V> {
    private ParameterParser<S, V> parser;
    private Suggester<S> suggester = Suggesters.empty();
    private int minUsage = 1;
    private int maxUsage = Integer.MAX_VALUE;

    /**
     * @param ignored
     * @param <S>
     * @param <V>
     * @return
     */
    public static <S extends CommandSender, V> ParameterBuilder<S, V> of(final ParameterizedCommand<S, V> ignored) {
        return new ParameterBuilder<>();
    }

    public ParameterBuilder<S, V> parser(final ParameterParser<S, V> parser) {
        this.parser = parser;
        return this;
    }

    public ParameterBuilder<S, V> suggester(final Suggester<S> suggester) {
        this.suggester = suggester;
        return this;
    }

    public ParameterBuilder<S, V> minimumUsage(final int minUsage) {
        this.minUsage = minUsage;
        return this;
    }

    public ParameterBuilder<S, V> maximumUsage(final int maxUsage) {
        this.maxUsage = maxUsage;
        return this;
    }

    public Parameter<S, V> build() {
        return new BuiltParameter(parser, suggester, minUsage, maxUsage);
    }

    private class BuiltParameter implements Parameter<S, V> {
        private final ParameterParser<S, V> parser;
        private final Suggester<S> suggester;
        private final int minUsage;
        private final int maxUsage;

        private BuiltParameter(final @NotNull ParameterParser<S, V> parser, final @NotNull Suggester<S> suggester, final int minUsage, final int maxUsage) {
            this.parser = parser;
            this.suggester = suggester;
            this.minUsage = minUsage;
            this.maxUsage = maxUsage;
        }

        @Override
        public @Nullable V parse(final S sender, final CommandInput input) {
            return parser.parse(sender, input);
        }

        @Override
        public Collection<String> getSuggestions(final S sender, final CommandInput input) {
            return suggester.getSuggestions(sender, input);
        }

        @Override
        public int getMinimumUsage() {
            return minUsage;
        }

        @Override
        public int getMaximumUsage() {
            return maxUsage;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                    .add("parser=" + parser)
                    .add("suggester=" + suggester)
                    .add("minUsage=" + minUsage)
                    .add("maxUsage=" + maxUsage)
                    .toString();
        }
    }
}
