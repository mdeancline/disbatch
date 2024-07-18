package io.github.disbatch.command;

import org.bukkit.command.CommandSender;

/**
 * Responsible for the execution of a command.
 *
 * @apiNote Not to be confused with {@link org.bukkit.command.CommandExecutor}.
 * @since 1.0.0
 * @param <S> any type extending {@link CommandSender} that can safely perform execution.
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 */
@FunctionalInterface
public interface CommandExecutor<S extends CommandSender, V> {

    /**
     * Executes the command.
     *
     * @param sender the {@link CommandSender} responsible for execution.
     * @param input  the {@link CommandInput} used to execute the {@code Command}.
     * @param value the resulting {@code Object} parsed from arguments.
     */
    void execute(S sender, CommandInput input, V value);

    @Deprecated
    default void execute(S sender, CommandInput input) {
        execute(sender, input, null);
    }
}
