package io.github.disbatch.command.parameter.model.paired;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.Parameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
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
 */
@ApiStatus.AvailableSince("1.0")
public final class PairedParameter<S extends CommandSender, F, L> implements Parameter<S, PairedArgument<F, L>> {
    private final Parameter<? super S, F> first;
    private final Parameter<? super S, L> last;

    /**
     * Creates a new {@code PairedParameter}.
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
    public Collection<String> getSuggestions(final S sender, final CommandInput input) {
        return input.getArgumentLength() < first.getMaximumUsage()
                ? first.getSuggestions(sender, input)
                : last.getSuggestions(sender, new ReducedArgumentsInput(first, input));
    }

    @Override
    public int getMinimumUsage() {
        return first.getMinimumUsage() + last.getMinimumUsage();
    }

    @Override
    public int getMaximumUsage() {
        return Math.min(first.getMaximumUsage() + last.getMaximumUsage(), Integer.MAX_VALUE);
    }
}
