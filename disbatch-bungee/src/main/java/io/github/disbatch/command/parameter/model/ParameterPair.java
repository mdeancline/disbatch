package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Allows the creation or retrieval of any two {@code Object}s, stored in a {@link Argument}, to be used in
 * the execution phase of a {@link ParameterizedCommand}.
 *
 * @param <S> {@inheritDoc}
 * @param <F> type of the first {@code Object} argument
 * @param <L> type of the last {@code Object} argument
 *
 * @since 1.0.0
 */
public final class ParameterPair<S extends CommandSender, F, L> implements Parameter<S, ParameterPair.Argument<F, L>> {
    private final Parameter<? super S, F> first;
    private final Parameter<? super S, L> last;

    /**
     * Creates a new {@link ParameterPair}.
     *
     * @param first the first {@code Parameter} that will produce the first {@code Object} argument
     * @param last  the last {@code Parameter} that will produce the last {@code Object} argument
     */
    public ParameterPair(final @NotNull Parameter<? super S, F> first, final @NotNull Parameter<? super S, L> last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public @Nullable Argument<F, L> parse(final S sender, final CommandInput input) {
        final F firstResult = first.parse(sender, input);
        final L lastResult = last.parse(sender, new ReducedArgumentsInput(first, input));

        if (firstResult == null || lastResult == null)
            return null;

        return new Argument<>(firstResult, lastResult);
    }

    @Override
    public Collection<String> tabComplete(final S sender, final CommandInput input) {
        return input.getArgumentLength() < first.getMaximumUsage()
                ? first.tabComplete(sender, input)
                : last.tabComplete(sender, new ReducedArgumentsInput(first, input));
    }

    @Override
    public int getMinimumUsage() {
        return first.getMinimumUsage() + last.getMinimumUsage();
    }

    @Override
    public int getMaximumUsage() {
        return Math.min(first.getMaximumUsage() + last.getMaximumUsage(), Integer.MAX_VALUE);
    }

    /**
     * Acts as a holder of the {@code Object} arguments created from any two {@code Parameter}s of a {@link ParameterPair}
     * when a {@link ParameterizedCommand} is executed.
     *
     * @param <F> type of the first {@code Object} argument
     * @param <L> type of the last {@code Object} argument
     *
     * @since 1.1.0
     */
    public static final class Argument<F, L> {
        private final F first;
        private final L last;

        Argument(final F first, final L last) {
            this.first = first;
            this.last = last;
        }

        /**
         * Retrieves the first {@code Object} argument.
         *
         * @return the first {@code Object} argument
         */
        public F getFirst() {
            return first;
        }

        /**
         * Retrieves the last {@code Object} argument.
         *
         * @return the last {@code Object} argument
         */
        public L getLast() {
            return last;
        }
    }

    private static class ReducedArgumentsInput implements CommandInput {
        private final Parameter<?, ?> first;
        private final CommandInput original;
        private String[] arguments;

        private ReducedArgumentsInput(final Parameter<?, ?> first, final CommandInput original) {
            this.first = first;
            this.original = original;
        }

        @Override
        public int getArgumentLength() {
            return original.getArgumentLength() - first.getMaximumUsage();
        }

        @Override
        public String getArgumentLine() {
            return original.getArgumentLine();
        }

        @Override
        public String getArgument(final int index) {
            if (index < 0 || index >= getArgumentLength())
                throw new ArgumentIndexOutOfBoundsException(index);

            return getArguments()[index];
        }

        @Override
        public String[] getArguments() {
            if (arguments == null) {
                final String[] arguments = (this.arguments = new String[0]);
                System.arraycopy(original.getArguments(), first.getMaximumUsage() - 1, arguments, 0, getArgumentLength());
            }

            return arguments;
        }

        @Override
        public String getCommandLabel() {
            return original.getCommandLabel();
        }

        @Override
        public String getCommandLine() {
            return original.getCommandLine();
        }
    }
}
