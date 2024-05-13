package io.github.disbatch.command.group;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.StringJoiner;

class GroupedCommandExecutor<S extends CommandSender> {
    private final GroupedCommand<S> command;
    private final CommandInput input;

    GroupedCommandExecutor(final GroupedCommand<S> command, final CommandInput input) {
        this.command = command;
        this.input = input;
    }

    void execute(final S sender) {
        command.execute(sender, new LazyLoadingGroupedCommandInput(input, command.getLabel()));
    }

    private static class LazyLoadingGroupedCommandInput implements CommandInput {
        private final CommandInput previous;
        private final String cmdLabel;
        private String[] arguments;
        private String argumentLine;

        private LazyLoadingGroupedCommandInput(final CommandInput previous, final String recentCmdLabel) {
            this.previous = previous;
            cmdLabel = previous.getCommandLabel() + " " + recentCmdLabel;
        }

        @Override
        public int getArgumentLength() {
            return previous.getArgumentLength() - 1;
        }

        @Override
        public String getArgumentLine() {
            return argumentLine == null
                    ? (argumentLine = String.join(" ", getArguments()))
                    : argumentLine;
        }

        @Override
        public String getArgument(final int index) {
            final String[] arguments = getArguments();

            if (index < 0 || index >= arguments.length)
                throw new ArgumentIndexOutOfBoundsException(index);

            return arguments[index];
        }

        @Override
        public String[] getArguments() {
            if (arguments == null) {
                final String[] previousArguments = previous.getArguments();
                final String[] arguments = (this.arguments = new String[previousArguments.length - 1]);
                System.arraycopy(previousArguments, 1, arguments, 0, arguments.length);

                return arguments;
            }

            return arguments;
        }

        @Override
        public String getCommandLabel() {
            return cmdLabel;
        }

        @Override
        public String getCommandLine() {
            return previous.getCommandLine();
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
