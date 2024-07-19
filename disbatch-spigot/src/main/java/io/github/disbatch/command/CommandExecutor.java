package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandSyntax;
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
    void execute(S sender, V value);

    /**
     * @deprecated This method has been replaced by {@link CommandExecutor#execute(CommandSender, Object)}.
     * To execute a command with a {@link CommandInput}, register it using a {@link CommandSyntax} for command inputs.
     * The required syntax can be retrieved using {@link CommandInputs#syntax()}.
     * <br><br>
     * Since this method now does nothing by default and is no longer called within the internal operations of Disbatch,
     * it should be avoided and replaced with the new method as soon as possible.
     */
    @Deprecated
    default void execute(S sender, CommandInput input) {
    }
}
