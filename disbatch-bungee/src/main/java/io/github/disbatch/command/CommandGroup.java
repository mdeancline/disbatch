package io.github.disbatch.command;

import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.InvalidInputHandler;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.decorator.MutableParameter;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Introduces the concept of executing various {@link Command}s belonging to a root {@code Command}.
 *
 * @param <S> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class CommandGroup<S extends CommandSender> extends ParameterizedCommand<S, CommandGroup.GroupedCommandExecutor<? super S>> {
    private final Map<String, GroupedCommand<? super S>> commands = new HashMap<>();

    public CommandGroup(final @NotNull InvalidInputHandler<? super S> handler) {
        this(new MutableParameter<>(), handler);
    }

    private CommandGroup(final MutableParameter<S, GroupedCommandExecutor<? super S>> parameter, final InvalidInputHandler<? super S> handler) {
        super(parameter, handler);

        parameter.setUnderlyingParameter(new Parameter.Builder<S, GroupedCommandExecutor<? super S>>()
                .parser((sender, input) -> {
                    final GroupedCommand<? super S> groupedCommand = commands.get(input.getArgument(0));
                    return groupedCommand == null
                            ? null
                            : new GroupedCommandExecutor<>(groupedCommand, input);
                })
                .tabCompleter(TabCompleters.forFirstArgument(TabCompleters.of(commands.keySet())))
                .build());
    }

    /**
     * Adds a {@link Command} to be linked to this one.
     *
     * @param command    the {@code Command} to be linked
     * @param descriptor
     */
    public CommandGroup<S> withCommand(final Command<? super S> command, final String label) {
        return withCommand(command, new CommandDescriptor.Builder().label(label).build());
    }

    /**
     * Adds a {@link Command} to be linked to this one.
     *
     * @param command    the {@code Command} to be linked
     * @param descriptor
     */
    public CommandGroup<S> withCommand(final Command<? super S> command, final CommandDescriptor descriptor) {
        commands.put(descriptor.getLabel(), new GroupedCommand<>(command, descriptor.getLabel()));

        for (final String alias : descriptor.getAliases())
            commands.put(alias, new GroupedCommand<>(command, alias));

        return this;
    }

    @Override
    protected void execute(final S sender, final GroupedCommandExecutor<? super S> executor, final CommandInput input) {
        executor.execute(sender);
    }

    private static class GroupedCommand<S extends CommandSender> {
        private final Command<S> innerCommand;
        private final String label;

        GroupedCommand(final @NotNull Command<S> innerCommand, final String label) {
            this.innerCommand = innerCommand;
            this.label = label;
        }

        void execute(final S sender, final CommandInput input) {
            innerCommand.execute(sender, input);
        }

        String getLabel() {
            return label;
        }
    }

    static class GroupedCommandExecutor<S extends CommandSender> {
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
}
