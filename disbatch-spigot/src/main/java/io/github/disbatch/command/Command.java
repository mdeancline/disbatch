package io.github.disbatch.command;

import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents an executable command within a Minecraft server running a Spigot implementation, which acts based on
 * various {@link CommandSender} inputs.
 *
 * @param <S> any type extending {@link CommandSender} that can safely execute the {@code Command}.
 * @apiNote Not to be confused with {@link org.bukkit.command.Command}.
 * @see CommandRegistrar
 * @since 1.0.0
 * @deprecated Starting in the release of Disbatch 1.2.0 for Spigot, all {@code Command} implementations will
 * have to implement the {@link Command#getSyntax()} method due to the introduction of only having to register a
 * {@link CommandExecutor} with a label at minimum. Other than that, the {@code Command} interface will remain available
 * in the library after that version release.
 */
@Deprecated
public interface Command<S extends CommandSender> extends CommandExecutor<S, String> {

    /**
     * @deprecated With the introduction of some of Minecraft's newest features, tab-completion suggestions will take
     * a more modular approach to utilize those features. Therefore, you should register a {@code Command} with an
     * implemented {@link Command#getSyntax()} that returns one capable of fetching a {@link Suggestion} collection.
     */
    @Deprecated
    default Collection<String> tabComplete(final S sender, final CommandInput input) {
        return Collections.emptyList();
    }

    @Deprecated
    default void execute(S sender, CommandInput input) {
        execute(sender, input, null);
    }

    //TODO return a valid CommandSyntax
    /**
     * Provides the {@code Argumentation} for this command
     *
     * @return the syntax for this command
     */
    default CommandSyntax<S, String> getSyntax() {
        return null;
    }
}
