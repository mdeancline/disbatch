package io.github.disbatch.command.group;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.parameter.InvalidInputHandler;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.builder.ParameterBuilder;
import io.github.disbatch.command.parameter.builder.Suggesters;
import io.github.disbatch.command.parameter.decorator.MutableParameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Introduces the concept of executing various {@link Command}s belonging to a root {@code Command}.
 *
 * @param <S> {@inheritDoc}
 */
@ApiStatus.AvailableSince("1.0")
public final class CommandGroup<S extends CommandSender> extends ParameterizedCommand<S, GroupedCommandExecutor<? super S>> {
    private final Map<String, GroupedCommand<? super S>> commands = new HashMap<>();

    public CommandGroup(final @NotNull InvalidInputHandler<? super S> handler) {
        this(new MutableParameter<>(), handler);
    }

    private CommandGroup(final MutableParameter<S, GroupedCommandExecutor<? super S>> parameter, final InvalidInputHandler<? super S> handler) {
        super(parameter, handler);

        parameter.setUnderlyingParameter(ParameterBuilder.of(this)
                .parser((sender, input) -> {
                    final GroupedCommand<? super S> groupedCommand = commands.get(input.getArgument(0));
                    return groupedCommand == null
                            ? null
                            : new GroupedCommandExecutor<>(groupedCommand, input);
                })
                .suggester(Suggesters.forFirstArgument(Suggesters.of(commands.keySet())))
                .build());
    }

    /**
     * Adds a {@link Command} to be linked to this one.
     *
     * @param command    the {@code Command} to be linked
     * @param descriptor
     */
    public void addCommand(final Command<? super S> command, final CommandDescriptor descriptor) {
        commands.put(descriptor.getLabel(), new GroupedCommand<>(command, descriptor.getLabel()));

        for (final String alias : descriptor.getAliases())
            commands.put(alias, new GroupedCommand<>(command, alias));
    }

    @Override
    protected void execute(final S sender, final GroupedCommandExecutor<? super S> executor, final CommandInput input) {
        executor.execute(sender);
    }
}
