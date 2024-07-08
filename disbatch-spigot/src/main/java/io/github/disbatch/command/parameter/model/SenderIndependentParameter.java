package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.AbstractParameter;
import io.github.disbatch.command.parameter.Parameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code Parameter} abstraction that doesn't rely on the {@link CommandSender} to create or retrieve an {@code Object}
 * based on parsable, passed arguments.
 *
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 *
 * @since 1.0.0
 */
public abstract class SenderIndependentParameter<V> extends AbstractParameter<CommandSender, V> {
    
    @Override
    public final @Nullable V parse(final CommandSender sender, final CommandInput input) {
        return parse(input);
    }

    /**
     * Serves the same functionality as {@link Parameter#parse(CommandSender, CommandInput)} but without the
     * {@link CommandSender}.
     *
     * @param input  the {@link CommandInput} passed from a {@link ParameterizedCommand}.
     * @return the parsed {@code Object} result.
     */
    protected abstract @Nullable V parse(CommandInput input);
}
