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
public interface CommandSyntax<S extends CommandSender, V> extends Iterable<CommandSyntax.Literal> {

    /**
     * Parses the {@link CommandInput} provided by the sender into an {@code Object} of type {@link V}, which may be {@code null},
     * for use in the command execution.
     *
     * @param sender the sender required to parse the input.
     * @param input  the input provided by the sender.
     * @return the parsed {@code Object}.
     */
    @Nullable V parse(S sender, CommandInput input);

    /**
     * Provides suggestions based on the current input.
     *
     * @param sender the command sender
     * @param arguments the current input arguments
     * @return a collection of suggestions
     */
    Collection<Suggestion> getSuggestions(S sender, String[] arguments);

    /**
     * Retrieves a specific literal by its index.
     *
     * @param index the index of the literal
     * @return the literal at the specified index, or {@code null} if it doesn't exist
     */
    @Nullable Literal getLiteral(int index);

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

    /**
     * Represents a literal in the command syntax.
     */
    interface Literal {

        /**
         * Checks if this literal is greedy, meaning it consumes all remaining arguments.
         *
         * @return true if the literal is greedy, false otherwise
         */
        boolean isGreedy();

        /**
         * Gets the child literals of this literal.
         *
         * @return a collection of child literals
         */
        Collection<Literal> getChildren();

        /**
         * Gets the label of this literal.
         *
         * @return the label of the literal
         */
        String getLabel();
    }
}