package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.syntax.AbstractSyntax;
import io.github.disbatch.command.syntax.CommandLiteral;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A namespace for {@link CommandInput} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class CommandInputs {
    private static final CommandInput EMPTY = new EmptyCommandInput();
    private static final CommandInputSyntax SYNTAX = new CommandInputSyntax();

    private CommandInputs() {
        throw new AssertionError();
    }

    /**
     * Retrieves an empty {@link CommandInput}.
     *
     * @return the retrieved {@link CommandInput}.
     */
    public static CommandInput empty() {
        return EMPTY;
    }

    /**
     * Creates a new {@link CommandInput} from the given label and arguments, using the provided {@link CommandSyntax}.
     *
     * @param label     the command label
     * @param arguments the command arguments
     * @param syntax    the command syntax
     * @return the created {@link CommandInput}
     * @since 1.1.0
     */
    public static CommandInput of(@NotNull final String label, @NotNull final String[] arguments, @NotNull final CommandSyntax<?, ?> syntax) {
        return arguments.length > 0
                ? new LazyLoadingCommandInput(label, arguments, syntax)
                : new SingleLabelCommandInput(label);
    }

    /**
     * Provides the flexible {@link CommandSyntax} for any sort of command input.
     *
     * @param <S> the type of the command sender
     * @return the syntax for command inputs
     * @since 1.1.0
     */
    public static CommandSyntax<CommandSender, CommandInput> syntax() {
        return SYNTAX;
    }

    private static class SingleLabelCommandInput implements CommandInput {
        private static final long serialVersionUID = -4557742310680630380L;

        private final String label;

        private SingleLabelCommandInput(final String label) {
            this.label = label;
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
        public Iterator<CommandInputBinding> iterator() {
            return EMPTY.iterator();
        }
    }

    private static class LazyLoadingCommandInput implements CommandInput {
        private static final long serialVersionUID = 7220435579159208472L;

        private final String[] arguments;
        private final CommandSyntax<?, ?> syntax;
        private final String cmdLabel;
        private CommandInputBinding[] bindings;
        private String argumentLine;
        private String commandLine;

        private LazyLoadingCommandInput(final String cmdLabel, final String[] arguments, final CommandSyntax<?, ?> syntax) {
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
        public Iterator<CommandInputBinding> iterator() {
            if (bindings == null) {
                bindings = new CommandInputBinding[arguments.length];

                for (int i = 0; i < bindings.length; i++) {
                    final CommandLiteral literal = syntax.getLiteral(i);
                    if (literal == null) break;
                    bindings[i] = new SimpleBinding(literal, arguments, i);
                }
            }

            return Arrays.asList(bindings).iterator();
        }
    }

    private static class EmptyCommandInput implements CommandInput {
        private static final long serialVersionUID = -8890684311013221621L;

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
        public Iterator<CommandInputBinding> iterator() {
            return Collections.emptyIterator();
        }
    }

    private static final class CommandInputSyntax extends AbstractSyntax<CommandSender, CommandInput> {
        private CommandInputSyntax() {
            super("args");
        }

        @Override
        protected boolean isGreedy() {
            return true;
        }

        @Nullable
        @Override
        public CommandInput parse(final CommandSender sender, final CommandInput input) {
            return input;
        }

        @Override
        public boolean matches(final CommandInputBinding binding) {
            return true;
        }

        @Override
        public int getMinimumUsage() {
            return 0;
        }

        @Override
        public int getMaximumUsage() {
            return Integer.MAX_VALUE;
        }
    }
}