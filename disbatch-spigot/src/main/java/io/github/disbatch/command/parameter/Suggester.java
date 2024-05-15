package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

/**
 * Serves the same purpose and functionality as {@link Parameter#getSuggestions(CommandSender, CommandInput)}.
 */
@FunctionalInterface
@ApiStatus.AvailableSince("1.0.0")
public interface Suggester<S extends CommandSender> {

    /**
     * See {@link Suggester}.
     */
    Collection<String> getSuggestions(S sender, CommandInput input);
}
