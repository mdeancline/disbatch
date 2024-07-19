package io.github.disbatch.command;

import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

//TODO complete development
/**
 * Introduces the concept of executing various commands belonging to a root command.
 *
 * @param <S> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class CommandGroup implements Command<CommandSender> {
    private final Map<String, CommandDescriptor.Executor> executors = new HashMap<>();

    public CommandGroup(final @NotNull String name) {

    }

    /**
     * Adds a command to be linked to this one through a {@link CommandDescriptor}.
     *
     * @param descriptor
     */
    public CommandGroup with(final @NotNull String label, final @NotNull CommandDescriptor descriptor) {
        final CommandDescriptor.Executor executor = descriptor.getExecutor();
        executors.put(label, descriptor.getExecutor());

        for (final String alias : descriptor.getAliases())
            executors.put(alias, executor);

        return this;
    }

    @Override
    public void run(final CommandSender sender, final CommandInput input) {
        executors.get(input.getArgument(0)).execute(sender, input);
    }

    private static class GroupedExecutor {
        private final CommandDescriptor.Executor command;

        private GroupedExecutor(final CommandDescriptor descriptor) {
            command = descriptor.getExecutor();
        }

        public void execute(final CommandSender sender, final CommandInput input) {
            command.execute(sender, new LazyLoadingGroupedArgumentInput(input));
        }
    }

    private static class LazyLoadingGroupedArgumentInput implements CommandInput {
        private final CommandInput previous;
        private final String cmdLabel;
        private String[] arguments;
        private String argumentLine;

        private LazyLoadingGroupedArgumentInput(final CommandInput previous) {
            this.previous = previous;
            cmdLabel = previous.getCommandLabel() + " " + previous.getArgument(0);
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

        @NotNull
        @Override
        public Iterator<Binding> iterator() {
            return null;
        }
    }
}
