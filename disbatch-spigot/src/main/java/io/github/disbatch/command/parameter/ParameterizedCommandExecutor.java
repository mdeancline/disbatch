package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;

/**
 * Responsible for the execution of any {@link ParameterizedCommand} created from a {@link ParameterizedCommand.Builder}.
 *
 * @param <S> any type extending {@link CommandSender} that can safely perform execution.
 * @param <V> the type from the resulting {@code Object} parsed from a set from arguments.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface ParameterizedCommandExecutor<S extends CommandSender, V> {

    /**
     * Executes the relative built {@link ParameterizedCommand}.
     *
     * @param sender   the {@link CommandSender} responsible for execution
     * @param argument the resulting argument
     * @param input    the {@link CommandInput} used to execute the built {@code ParameterizedCommand}
     */
    void execute(S sender, V argument, CommandInput input);
}
