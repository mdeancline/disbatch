package io.github.disbatch.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

/**
 * Responsible for the execution of any {@link Command} created from a {@link Command.Builder}.
 *
 * @param <S> any type extending {@link CommandSender} that can safely perform execution.
 * @apiNote Not to be confused with {@link org.bukkit.command.CommandExecutor}.
 */
@FunctionalInterface
@ApiStatus.AvailableSince("1.0.0")
public interface CommandExecutor<S extends CommandSender> {

    /**
     * Executes the relative built {@link Command}.
     *
     * @param sender the {@link CommandSender} responsible for execution.
     * @param input  the {@link CommandInput} used to execute the {@code Command}.
     */
    void execute(S sender, CommandInput input);
}
