package io.github.disbatch.command;

import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents an executable command within a Minecraft server running a Spigot implementation, based on
 * various {@link CommandSender} inputs.
 *
 * @param <S> any type extending {@link CommandSender} that can safely execute the {@code Command}.
 * @apiNote Not to be confused with {@link org.bukkit.command.Command}.
 * @see CommandRegistrar
 * @since 1.0.0
 * @deprecated This will be fully replaced by {@link CommandExecutor} for flexible {@code Object} parsing.
 * Tab completion will be managed by a {@link CommandSyntax} returning a collection of {@link Suggestion}s,
 * which supports current and future syntax highlighting features, including those powered by
 * <a href="https://github.com/Mojang/brigadier">Brigadier</a>.
 */
@Deprecated
public interface Command<S extends CommandSender> extends SyntaxExecutor<S, CommandInput> {

    /**
     * Executed on tab completion, returning a {@code List} of argument options the {@link CommandSender} can tab through.
     *
     * @param sender the {@link CommandSender} responsible for initiating a tab completion.
     * @param input  the {@link CommandInput} present from tab completion.
     * @return a list of tab completions for the specified arguments, which may be empty or immutable.
     */
    default Collection<String> tabComplete(final S sender, final CommandInput input) {
        return Collections.emptyList();
    }

    @Override
    default CommandSyntax<? super S, CommandInput> getSyntax() {
        return CommandInputs.syntax();
    }

    @Override
    default void execute(final S sender, final CommandInput input) {
    }
}
