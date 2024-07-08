package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.Parameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Parses a {@link Parameter} array based on parsable, passed arguments.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class ArrayParameter<S extends CommandSender, V> implements Parameter<S, V[]> {
    private static final String WHITESPACE = " ";

    private final Parameter<S, V> innerParameter;
    private final int minUsageMultiple;
    private final int maxUsageMultiple;

    public ArrayParameter(final @NotNull Parameter<S, V> innerParameter) {
        this(innerParameter, 1, Integer.MAX_VALUE);
    }

    public ArrayParameter(final @NotNull Parameter<S, V> innerParameter, final int minUsageMultiple, final int maxUsageMultiple) {
        this.innerParameter = innerParameter;
        this.minUsageMultiple = minUsageMultiple;
        this.maxUsageMultiple = maxUsageMultiple;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable V[] parse(final S sender, final CommandInput input) {
        final int maxUsageBound = getMaximumUsage() - 1;
        final V[] objArguments = (V[]) new Object[maxUsageMultiple];

        for (int i = 0, j = maxUsageBound; i < maxUsageMultiple; i++, j += maxUsageBound) {
            final String selectedArgLine = input.getArgumentLine().substring(j, j + maxUsageBound);
            final String[] selectedArgs = selectedArgLine.split(WHITESPACE);

            final V result = innerParameter.parse(sender, new SelectedArgumentsInput(input, selectedArgs));
            if (result == null) return null;
            objArguments[i] = result;
        }

        return objArguments;
    }

    @Override
    public Collection<String> tabComplete(final S sender, final CommandInput input) {
        final int argLength = input.getArgumentLength();
        final int remainder = argLength % innerParameter.getMaximumUsage();
        final int beginIndex = (argLength - remainder) - 1;
        final String selectedArgLine = input.getArgumentLine().substring(beginIndex, argLength - 1);
        final String[] selectedArgs = selectedArgLine.split(WHITESPACE);

        return innerParameter.tabComplete(sender, new SelectedArgumentsInput(input, selectedArgs));
    }

    @Override
    public int getMinimumUsage() {
        return minUsageMultiple;
    }

    @Override
    public int getMaximumUsage() {
        return Math.min(innerParameter.getMaximumUsage() * maxUsageMultiple, Integer.MAX_VALUE);
    }

    static class SelectedArgumentsInput implements CommandInput {
        private final CommandInput original;
        private final String[] selectedArgs;
        private String argumentLine;
        private String commandLine;

        SelectedArgumentsInput(final CommandInput original, final String[] selectedArgs) {
            this.original = original;
            this.selectedArgs = selectedArgs;
        }

        @Override
        public int getArgumentLength() {
            return selectedArgs.length;
        }

        @Override
        public String getArgumentLine() {
            return argumentLine == null
                    ? (argumentLine = String.join(" "))
                    : argumentLine;
        }

        @Override
        public String getArgument(final int index) {
            if (index < 0 || index >= selectedArgs.length)
                throw new ArgumentIndexOutOfBoundsException(index);

            return selectedArgs[index];
        }

        @Override
        public String[] getArguments() {
            return selectedArgs;
        }

        @Override
        public String getCommandLabel() {
            return original.getCommandLabel();
        }

        @Override
        public String getCommandLine() {
            return commandLine == null
                    ? (commandLine = original.getCommandLabel() + " " + getArgumentLine())
                    : commandLine;
        }
    }
}
