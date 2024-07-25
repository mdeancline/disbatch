package io.github.disbatch.command.syntax;

import io.github.disbatch.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputBinding;
import io.github.disbatch.command.CommandInputs;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Defines the syntax of a {@link Command}, enabling the creation or retrieval of objects required during command
 * execution without the need to perform various checks to ensure that the arguments are suitable for parsing. The
 * syntax can depend on the {@link CommandSender}.
 *
 * @param <S> the type extending {@link CommandSender} required to parse arguments.
 * @param <V> the type of the resulting object parsed from arguments.
 * @see CommandInputs#syntax()
 * @since 1.1.0
 */
public interface CommandSyntax<S extends CommandSender, V> {

    /**
     * Parses the {@link CommandInput} provided by the sender into an object of type {@link V}, which may be {@code null},
     * for use in the command execution.
     *
     * @param sender the sender required to parse the input.
     * @param input  the input provided by the sender.
     * @return the parsed object, or {@code null} if parsing fails.
     */
    @Nullable
    V parse(S sender, CommandInput input);

    /**
     * Provides suggestions based on the current input.
     *
     * @param sender  the command sender.
     * @param binding the current input arguments.
     * @return a collection of suggestions.
     */
    Collection<Suggestion> getSuggestions(S sender, CommandInputBinding binding);

    /**
     * Retrieves a specific literal by its index.
     *
     * @param index the index of the literal.
     * @return the literal at the specified index, or {@code null} if it doesn't exist.
     */
    @Nullable
    CommandLiteral getLiteral(int index);

    /**
     * Checks if the binding matches the expected syntax.
     *
     * @param binding the command argument binding.
     * @return true if the binding matches, false otherwise.
     */
    boolean matches(CommandInputBinding binding);

    /**
     * Gets the minimum number of arguments required for this syntax.
     *
     * @return the minimum number of arguments.
     */
    int getMinimumUsage();

    /**
     * Gets the maximum number of arguments allowed for this syntax.
     *
     * @return the maximum number of arguments.
     */
    int getMaximumUsage();
}
