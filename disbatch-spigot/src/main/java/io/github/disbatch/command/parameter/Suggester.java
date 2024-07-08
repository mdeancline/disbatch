package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.TabCompleter;
import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * Serves the same purpose and functionality as {@link Parameter#getSuggestions(CommandSender, CommandInput)}.
 *
 * @since 1.0.0
 *
 * @deprecated planned to be replaced with {@link TabCompleter}
 */
@Deprecated
@FunctionalInterface
public interface Suggester<S extends CommandSender> {

    /**
     * See {@link Suggester}.
     *
     *
     */
    Collection<String> getSuggestions(S sender, CommandInput input);
}
