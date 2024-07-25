package io.github.disbatch.command.syntax;

import io.github.disbatch.command.CommandInputBinding;
import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * Responsible for retrieving a collection of suggestions that a {@link CommandSender} can view for command input
 * completion on their client, typically through tab completion.
 *
 * @param <S> any type extending {@link CommandSender} that can safely receive suggestions.
 * @since 1.1.0
 */
@FunctionalInterface
public interface Suggester<S extends CommandSender> {

    /**
     * Retrieves a collection of possible suggestions based on the sender's input that they can view on their client.
     *
     * @param sender  the {@link CommandSender} responsible for initiating a tab completion
     * @param binding the {@link CommandInputBinding} containing the current command input context
     * @return a {@code Collection} of tab completion options for the specified arguments, which may be empty or immutable
     */
    Collection<Suggestion> getSuggestions(S sender, CommandInputBinding binding);
}
