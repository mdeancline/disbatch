package io.github.disbatch.command;

import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * Responsible retrieving a collection of suggestions a {@link CommandSender} view for command input completion on
 * their client, which is typically done through tab completion.
 *
 * @param <S> any type extending {@link CommandSender} that can safely receive suggestions.
 * @since 1.1.0
 */
@FunctionalInterface
public interface Suggester<S extends CommandSender> {

    /**
     * Retrieves a collection of possible suggestions based on the sender's input that they can view on their client.
     *
     * @param sender the {@link CommandSender} responsible for initiating a tab completion
     * @param input  the {@link CommandInput} present from tab completion
     * @return a {@code Collection} of tab completion options for the specified arguments, which may be empty or immutable
     */
    Collection<Suggestion> getSuggestions(S sender, String[] arguments);
}
