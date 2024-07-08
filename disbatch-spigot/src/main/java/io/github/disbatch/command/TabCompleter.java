package io.github.disbatch.command;

import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * Responsible for the tab completion of any {@link Command} created from a {@link Command.Builder}.
 *
 * @param <S> any type extending {@link CommandSender} that can safely perform tab completion.
 * @apiNote Not to be confused with {@link org.bukkit.command.TabCompleter}.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface TabCompleter<S extends CommandSender> {

    /**
     * Executed on tab completion, returning a {@code Collection} of argument options the {@link CommandSender} can tab through.
     *
     * @param sender the {@link CommandSender} responsible for initiating a tab completion.
     * @param input  the {@link CommandInput} present from tab completion.
     * @return a collection of tab completions for the specified arguments, which may be empty or immutable.
     */
    Collection<String> tabComplete(S sender, CommandInput input);
}
