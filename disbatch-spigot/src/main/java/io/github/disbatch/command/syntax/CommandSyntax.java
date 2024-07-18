package io.github.disbatch.command.syntax;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.Suggestion;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Allows the creation or retrieval of any {@code Object} to be used in the execution phase of a command without the
 * burden of performing various checks to ensure that a specific set of arguments is fit for parsing, which can also
 * depend on the {@link CommandSender}.
 *
 * @since 1.1.0
 * @param <S> any type extending {@link CommandSender} required to parse arguments.
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 */
public interface CommandSyntax<S extends CommandSender, V> extends Iterable<CommandSyntax.Node> {

    /**
     * Parses the command input to a value of type {@code V}.
     *
     * @param sender the command sender
     * @param input the command input
     * @return the parsed value, or null if parsing fails
     */
    @Nullable V parse(S sender, CommandInput input);

    /**
     * Provides suggestions based on the current input.
     *
     * @param sender the command sender
     * @param args the current input arguments
     * @return a collection of suggestions
     */
    Collection<Suggestion> getSuggestions(S sender, String[] args);

    @Nullable Node getNode(int argumentIndex);

    /**
     * Checks if the given argument binding matches the expected syntax.
     *
     * @param binding the command argument binding
     * @return true if the binding matches, false otherwise
     */
    boolean matches(CommandInput.Binding binding);

    /**
     * Gets the minimum number of arguments required for this syntax.
     *
     * @return the minimum number of arguments
     */
    int getMinimumUsage();

    /**
     * Gets the maximum number of arguments allowed for this syntax.
     *
     * @return the maximum number of arguments
     */
    int getMaximumUsage();

    interface Node extends Iterable<Node> {
        boolean hasChild();

        boolean hasChildren();

        Node getChild(String value);

        Node next();

        String getLabel();
    }
}