package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A namespace for {@link CommandInput} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class CommandInputs {
    private static CommandInput EMPTY = new EmptyCommandInput();
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

    private static class EmptyCommandInput implements CommandInput {
        @Override
        public int getArgumentLength() {
            return 0;
        }

        @Override
        public String getArgumentLine() {
            return "";
        }

        @Override
        public String getArgument(int index) {
            if (index > 0)
                throw new IndexOutOfBoundsException();

            return "";
        }

        @Override
        public String[] getArguments() {
            return new String[]{};
        }

        @Override
        public String getCommandLabel() {
            return "";
        }

        @Override
        public String getCommandLine() {
            return "";
        }
    }

    public static class SingleLabelArgumentInput implements CommandInput {
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
    }

    public static class LazyLoadingArgumentInput implements CommandInput {
        private final String[] arguments;
        private final String cmdLabel;
        private String argumentLine;
        private String commandLine;

        private LazyLoadingArgumentInput(final String[] arguments, final String cmdLabel) {
            this.arguments = arguments;
            this.cmdLabel = cmdLabel;
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
    }
}
