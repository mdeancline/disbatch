package io.github.disbatch.command.parameter;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.exception.InvalidParameterException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Introduces the concept of effortlessly transforming, or parsing, a specific set of arguments of a compatible length
 * into a usable {@code Object} during execution. If a passed {@link CommandInput} cannot be parsed, the inner
 * {@link InvalidInputHandler} will be used to take control of that situation.
 *
 * @param <S> {@inheritDoc}
 * @param <V> the type from the resulting {@code Object} parsed from a set from arguments.
 * @see #execute(CommandSender, Object, CommandInput)
 * @see Parameter
 *
 * @since 1.0.0
 */
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
    public final Collection<String> tabComplete(final S sender, final @NotNull CommandInput input) {
        Collection<String> completions = parameter.tabComplete(sender, input);

        // temporary solution until the getSuggestions method is removed
        if (completions instanceof ImmutableList && completions.size() == 0)
            completions = parameter.getSuggestions(sender, input);

        return input.getArgumentLength() <= parameter.getMaximumUsage()
                ? completions
                : Command.super.tabComplete(sender, input);
    }

    /**
     * Serves as a flexible solution for creating a new {@link ParameterizedCommand} without defining an anonymous or explicit abstraction.
     *
     * @param <S> any type extending {@link CommandSender} that can safely execute any built {@link ParameterizedCommand}.
     *
     * @since 1.0.0
     */
    public static final class Builder<S extends CommandSender, V> {
        private Parameter<? super S, V> parameter;
        private InvalidInputHandler<? super S> handler;
        private ParameterizedCommandExecutor<S, V> executor;

        public Builder<S, V> parameter(final @NotNull Parameter<? super S, V> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Builder<S, V> executor(final @NotNull ParameterizedCommandExecutor<S, V> executor) {
            this.executor = executor;
            return this;
        }

        public Builder<S, V> invalidInputHandler(final @NotNull InvalidInputHandler<? super S> handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Creates a new {@link ParameterizedCommand}.
         *
         * @return the created {@code ParameterizedCommand}.
         */
        public ParameterizedCommand<S, V> build() {
            return new BuiltCommand<>(parameter, handler, executor);
        }

        private static class BuiltCommand<S extends CommandSender, V> extends ParameterizedCommand<S, V> {
            private final ParameterizedCommandExecutor<S, V> executor;

            private BuiltCommand(final @NotNull Parameter<? super S, V> parameter, final @NotNull InvalidInputHandler<? super S> handler, @NotNull ParameterizedCommandExecutor<S, V> executor) {
                super(parameter, handler);
                this.executor = executor;
            }

            @Override
            protected void execute(S sender, V argument, CommandInput input) {
                executor.execute(sender, argument, input);
            }
        }
    }

    private static class InvalidInputImpl implements InvalidInput {
        private final CommandInput original;
        private final Reason reason;

        InvalidInputImpl(final CommandInput original, final Reason reason) {
            this.original = original;
            this.reason = reason;
        }

        @Override
        public int getArgumentLength() {
            return original.getArgumentLength();
        }

        @Override
        public String getArgumentLine() {
            return original.getArgumentLine();
        }

        @Override
        public String getArgument(final int index) {
            return original.getArgument(index);
        }

        @Override
        public String[] getArguments() {
            return original.getArguments();
        }

        @Override
        public String getCommandLabel() {
            return original.getCommandLabel();
        }

        @Override
        public String getCommandLine() {
            return original.getCommandLine();
        }

        @Override
        public Reason getReason() {
            return reason;
        }
    }
}
