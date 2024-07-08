package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * Allows the creation or retrieval of any {@code Object} to be used in the execution phase of a {@link ParameterizedCommand}
 * without the burden of performing various checks to ensure that a specific set of arguments is fit for parsing,
 * which can also depend on the {@link CommandSender}.
 *
 * @param <S> any type extending {@link CommandSender} required to parse arguments.
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 * @see Parameter.Builder
 * @see AbstractParameter
 *
 * @since 1.0.0
 */
public interface Parameter<S extends CommandSender, V> {

    /**
     * Parses the given {@link CommandInput} into an {@code Object} argument, matching the type parameter {@link V},
     * which can also be {@code null}.
     *
     * @param sender the {@link CommandSender} required to parse the arguments.
     * @param input  the {@link CommandInput} passed from a {@link ParameterizedCommand}.
     * @return the parsed {@code Object} result.
     */
    @Nullable V parse(S sender, CommandInput input);

    /**
     * Retrieves a {@code Collection} of all the possible suggestions to be used for tab completion from a
     * {@link ParameterizedCommand}.
     *
     * @param sender the {@link CommandSender} responsible for requesting suggestions.
     * @param input  the {@link CommandInput} passed from a {@link ParameterizedCommand} during tab completion.
     * @return all possible suggestions.
     */
    Collection<String> getSuggestions(S sender, CommandInput input);

    /**
     * Retrieves the minimum number of arguments a {@link ParameterizedCommand} must pass to the {@link Parameter}.
     *
     * @return the minimum argument amount.
     */
    int getMinimumUsage();

    /**
     * Retrieves the maximum amount of arguments a {@link ParameterizedCommand} can pass to the {@link Parameter}.
     *
     * @return the maximum argument amount.
     */
    int getMaximumUsage();

    /**
     * Serves as the flexible solution for creating a {@link Parameter}.
     *
     * @param <S> any type extending {@link CommandSender} required to parse arguments.
     * @param <V> the type from the resulting {@code Object} parsed from arguments.
     *
     * @since 1.0.0
     */
    final class Builder<S extends CommandSender, V> {
        private ParameterParser<S, V> parser;
        private Suggester<S> suggester = Suggesters.empty();
        private int minUsage = 1;
        private int maxUsage = Integer.MAX_VALUE;

        public Builder<S, V> parser(final ParameterParser<S, V> parser) {
            this.parser = parser;
            return this;
        }

        public Builder<S, V> suggester(final Suggester<S> suggester) {
            this.suggester = suggester;
            return this;
        }

        public Builder<S, V> minimumUsage(final int minUsage) {
            this.minUsage = minUsage;
            return this;
        }

        public Builder<S, V> maximumUsage(final int maxUsage) {
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
}
