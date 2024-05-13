package io.github.disbatch.command.parameter;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.exception.InvalidParameterException;
import io.github.disbatch.command.parameter.model.Parameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Introduces the concept of effortlessly transforming, or parsing, a specific set of arguments of a compatible length
 * into a usable {@code Object} during execution. If a passed {@link CommandInput} cannot be parsed, the inner
 * {@link InvalidInputHandler} will be used to take control of that situation.
 *
 * @param <S> {@inheritDoc}
 * @param <V> the type from the resulting {@code Object} parsed from a set from arguments
 * @see #execute(CommandSender, Object, CommandInput)
 * @see Parameter
 */
@ApiStatus.AvailableSince("1.0")
public abstract class ParameterizedCommand<S extends CommandSender, V> implements Command<S> {
    private final Parameter<? super S, V> parameter;
    private final InvalidInputHandler<? super S> handler;

    /**
     * Constructs a new {@link ParameterizedCommand}.
     *
     * @param parameter the {@link Parameter} to use for {@code Object} argument creation
     * @param handler  the {@link InvalidInputHandler} to use for processing a {@link CommandSender} and an {@link InvalidInput}
     *                  when necessary.
     * @throws InvalidParameterException if the passed {@link Parameter}'s minimum or maximum usages either return {@code 0} or
     *                                   exceed one another.
     * @see Parameter#getMinimumUsage()
     * @see Parameter#getMaximumUsage()
     */
    protected ParameterizedCommand(final @NotNull Parameter<? super S, V> parameter, final @NotNull InvalidInputHandler<? super S> handler) {
        final int minUsage = parameter.getMinimumUsage();
        final int maxUsage = parameter.getMaximumUsage();

        if (minUsage <= 0) throw new InvalidParameterException("Minimum usage must be greater than 0");
        if (maxUsage <= 0) throw new InvalidParameterException("Maximum usage must be greater than 0");
        if (minUsage > maxUsage) throw new InvalidParameterException("Minimum usage cannot exceed maximum usage");

        this.parameter = parameter;
        this.handler = handler;
    }

    @Override
    public final void execute(final S sender, final CommandInput input) {
        final int length = input.getArgumentLength();

        final boolean hasLackingArgs = length < parameter.getMinimumUsage();
        final boolean hasExtraArgs = length > parameter.getMaximumUsage();

        if (!hasLackingArgs && !hasExtraArgs) {
            final V result = parameter.parse(sender, input);

            if (result == null) handler.handle(sender, new InvalidInputImpl(input, InvalidInput.Reason.INSUFFICIENT_ARGUMENTS));
            else execute(sender, result, input);
        } else if (hasLackingArgs)
            handler.handle(sender, new InvalidInputImpl(input, InvalidInput.Reason.LACKING_ARGUMENTS));
        else
            handler.handle(sender, new InvalidInputImpl(input, InvalidInput.Reason.EXTRA_ARGUMENTS));
    }

    /**
     * Serves the same functionality as {@link Command#execute(CommandSender, CommandInput)} but with an additional resulting
     * {@code Object} argument of type parameter {@link V}.
     *
     * @param sender   the {@link CommandSender} responsible for execution
     * @param argument the resulting argument
     * @param input    the {@link CommandInput} used to execute the {@code ParameterizedCommand}
     */
    protected abstract void execute(final S sender, final V argument, final CommandInput input);

    @Override
    public final List<String> tabComplete(final S sender, final @NotNull CommandInput input) {
        return input.getArgumentLength() <= parameter.getMaximumUsage()
                ? new LinkedList<>(parameter.getSuggestions(sender, input))
                : ImmutableList.of();
    }
}
