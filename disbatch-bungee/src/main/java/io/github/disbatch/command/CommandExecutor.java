package io.github.disbatch.command;

import net.md_5.bungee.api.CommandSender;

/**
 * Responsible for the execution of any {@link Command} created from a {@link Command.Builder}.
 *
 * @param <S> any type extending {@link CommandSender} that can safely perform execution.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface CommandExecutor<S extends CommandSender> {

    /**
     * Executes the relative built {@link Command}.
     *
     * @param sender the {@link CommandSender} responsible for execution.
     * @param input  the {@link CommandInput} used to execute the {@code Command}.
     */
    void execute(S sender, CommandInput input);
}
