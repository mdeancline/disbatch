package io.github.disbatch.command;

import com.google.common.collect.Iterators;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A namespace for {@link CommandInput} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class CommandInputs {
    private static final CommandInput EMPTY = new EmptyCommandInput();
    private CommandInputs() {
        throw new AssertionError();
    }

    /**
     * Retrieves an empty {@link CommandInput}.
     * @return the retrieved {@link CommandInput}.
     */
    public static CommandInput empty() {
        return EMPTY;
    }

    /**
     * @since 1.1.0
     * @param label
     * @param arguments
     * @return
     */
    public static CommandInput of(final @NotNull String label, final @NotNull String[] arguments, final @NotNull CommandSyntax<?, ?> syntax) {
        return arguments.length > 0
                ? new LazyLoadingArgumentInput(label, arguments, syntax)
                : new SingleLabelArgumentInput(label);
    }

    private static class SingleLabelArgumentInput implements CommandInput {
        private final String label;

        private SingleLabelArgumentInput(final String label) {
            this.label = label;
        }

        @Override
        public int getArgumentLength() {
            return 0;
        }

        @Override
        public String getArgumentLine() {
            return emptyString();
        }

        @Override
        public String getArgument(final int index) {
            return emptyString();
        }

        private String emptyString() {
            return StringUtils.EMPTY;
        }

        @Override
        public String[] getArguments() {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        @Override
        public String getCommandLabel() {
            return label;
        }

        @Override
        public String getCommandLine() {
            return label;
        }

        @NotNull
        @Override
        public Iterator<Binding> iterator() {
            return Iterators.emptyIterator();
        }
    }

    private static class LazyLoadingArgumentInput implements CommandInput {
        private final String[] arguments;
        private final CommandSyntax<?, ?> syntax;
        private final String cmdLabel;
        private CommandInput.Binding[] bindings;
        private String argumentLine;
        private String commandLine;

        private LazyLoadingArgumentInput(final String cmdLabel, final String[] arguments, final CommandSyntax<?, ?> syntax) {
            this.arguments = arguments;
            this.cmdLabel = cmdLabel;
            this.syntax = syntax;
        }

        @Override
        public String getArgument(final int index) {
            if (index < 0 || index >= arguments.length)
                throw new ArgumentIndexOutOfBoundsException(index);

            return arguments[index];
        }

        @Override
        public String[] getArguments() {
            return arguments;
        }
        @Override
        public String getCommandLabel() {
            return cmdLabel;
        }

        @Override
        public int getArgumentLength() {
            return arguments.length;
        }

        @Override
        public String getArgumentLine() {
            if (argumentLine == null)
                return argumentLine = arguments.length == 1
                        ? arguments[0]
                        : String.join(" ", arguments);

            return argumentLine;
        }

        @Override
        public String getCommandLine() {
            return commandLine == null
                    ? (commandLine = String.join(" ", cmdLabel, getArgumentLine()))
                    : commandLine;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof CommandInput)) return false;
            final CommandInput that = (CommandInput) o;
            return getCommandLine().equals(that.getCommandLine());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCommandLine());
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                    .add("commandLine=" + getCommandLine())
                    .toString();
        }

        @NotNull
        @Override
        public Iterator<Binding> iterator() {
            if (bindings == null) {
                bindings = new CommandInput.Binding[arguments.length];

                for (int i = 0; i < bindings.length; i++)
                    bindings[i] = new SimpleBinding(syntax.getNode(i), arguments, i);
            }

            return Arrays.asList(bindings).iterator();
        }
    }

    private static class EmptyCommandInput implements CommandInput {
        @Override
        public int getArgumentLength() {
            return 0;
        }

        @Override
        public String getArgumentLine() {
            return StringUtils.EMPTY;
        }

        @Override
        public String getArgument(final int index) {
            throw new ArgumentIndexOutOfBoundsException(index);
        }

        @Override
        public String[] getArguments() {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        @Override
        public String getCommandLabel() {
            return StringUtils.EMPTY;
        }

        @Override
        public String getCommandLine() {
            return StringUtils.EMPTY;
        }

        @NotNull
        @Override
        public Iterator<CommandInput.Binding> iterator() {
            return Iterators.emptyIterator();
        }
    }
}
