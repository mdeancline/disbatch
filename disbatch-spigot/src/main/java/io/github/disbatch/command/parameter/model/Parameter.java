package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.builder.ParameterBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Allows the creation or retrieval of any {@code Object} to be used in the execution phase of a {@link ParameterizedCommand}
 * without the burden of performing various checks to ensure that a specific set of arguments is fit for parsing,
 * which can also depend on the {@link CommandSender}.
 *
 * @param <S> any type extending {@link CommandSender} required to parse arguments.
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 * @see ParameterBuilder
 * @see AbstractParameter
 */
@ApiStatus.AvailableSince("1.0")
public interface Parameter<S extends CommandSender, V> {

    /**
     * Parses the given {@link CommandInput} into an {@code Object} argument, matching the type parameter {@link V},
     * which can also be {@code null}.
     *
     * @param sender the {@link CommandSender} required to parse the arguments.
     * @param input  the {@link CommandInput} passed from a {@link ParameterizedCommand}.
     * @return the parsed {@code Object} result.
     */
    @Nullable V parse(S sender, CommandInput input);

    /**
     * Retrieves a {@code Collection} of all the possible suggestions to be used for tab completion from a
     * {@link ParameterizedCommand}.
     *
     * @param sender the {@link CommandSender} responsible for requesting suggestions.
     * @param input  the {@link CommandInput} passed from a {@link ParameterizedCommand} during tab completion.
     * @return all possible suggestions.
     */
    Collection<String> getSuggestions(S sender, CommandInput input);

    /**
     * Retrieves the minimum number of arguments a {@link ParameterizedCommand} must pass to the {@link Parameter}.
     *
     * @return the minimum argument amount.
     */
    int getMinimumUsage();

    /**
     * Retrieves the maximum amount of arguments a {@link ParameterizedCommand} can pass to the {@link Parameter}.
     *
     * @return the maximum argument amount.
     */
    int getMaximumUsage();
}
