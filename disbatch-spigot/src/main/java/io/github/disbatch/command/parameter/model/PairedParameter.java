package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Allows the creation or retrieval of any two {@code Object}s, stored in a {@link PairedArgument}, to be used in
 * the execution phase of a {@link ParameterizedCommand}.
 *
 * @param <S> {@inheritDoc}
 * @param <F> type of the first {@code Object} argument
 * @param <L> type of the last {@code Object} argument
 *
 * @since 1.0.0
 */
public final class PairedParameter<S extends CommandSender, F, L> implements Parameter<S, PairedArgument<F, L>> {
    private final Parameter<? super S, F> first;
    private final Parameter<? super S, L> last;

    /**
     * Creates a new {@link PairedParameter}.
     *
     * @param first the first {@code Parameter} that will produce the first {@code Object} argument
     * @param last  the last {@code Parameter} that will produce the last {@code Object} argument
     */
    public PairedParameter(final @NotNull Parameter<? super S, F> first, final @NotNull Parameter<? super S, L> last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public @Nullable PairedArgument<F, L> parse(final S sender, final CommandInput input) {
        final F firstResult = first.parse(sender, input);
        final L lastResult = last.parse(sender, new ReducedArgumentsInput(first, input));

        if (firstResult == null || lastResult == null)
            return null;

        return new PairedArgument<>(firstResult, lastResult);
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

    static class ReducedArgumentsInput implements CommandInput {
        private final Parameter<?, ?> first;
        private final CommandInput original;
        private String[] arguments;

        ReducedArgumentsInput(final Parameter<?, ?> first, final CommandInput original) {
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
