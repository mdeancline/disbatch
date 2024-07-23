package io.github.disbatch.command.syntax.model;

import com.google.common.collect.Iterators;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.Suggestion;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

/**
 * Parses an array of parsable {@code Object}s based on parsable, passed arguments.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @since 1.1.0
 */
public final class ArraySyntax<S extends CommandSender, V> implements CommandSyntax<S, V[]> {
    private static final String WHITESPACE = " ";

    private final CommandSyntax<S, V> source;
    private final int minUsageMultiple;
    private final int maxUsageMultiple;

    public ArraySyntax(@NotNull final CommandSyntax<S, V> source) {
        this(source, 1, Integer.MAX_VALUE);
    }

    public ArraySyntax(@NotNull final CommandSyntax<S, V> source, final int minUsageMultiple, final int maxUsageMultiple) {
        this.source = source;
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

            final V result = source.parse(sender, new SelectedArgumentsInput(input, selectedArgs));
            if (result == null) return null;
            objArguments[i] = result;
        }

        return objArguments;
    }

    @Override
    public Collection<Suggestion> getSuggestions(final S sender, final String[] arguments) {
        final int length = arguments.length;
        final int remainder = length % source.getMaximumUsage();
        final int beginIndex = (length - remainder) - 1;
        final String selectedArgumentLine = String.join(" ", arguments).substring(beginIndex, length - 1);
        final String[] selectedArgs = selectedArgumentLine.split(WHITESPACE);

        return source.getSuggestions(sender, selectedArgs);
    }

    @Override
    public @Nullable CommandSyntax.Literal getLiteral(final int index) {
        return source.getLiteral(index);
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return false;
    }

    @Override
    public int getMinimumUsage() {
        return minUsageMultiple;
    }

    @Override
    public int getMaximumUsage() {
        return Math.min(source.getMaximumUsage() * maxUsageMultiple, Integer.MAX_VALUE);
    }

    //TODO return valid iterator
    @NotNull
    @Override
    public Iterator<Literal> iterator() {
        return Iterators.emptyIterator();
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

        @NotNull
        @Override
        public Iterator<CommandInput.Binding> iterator() {
            return null;
        }
    }
}
