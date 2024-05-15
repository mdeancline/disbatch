package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Serves the same purpose and functionality as {@link Parameter#parse(CommandSender, CommandInput)}.
 */
@FunctionalInterface
@ApiStatus.AvailableSince("1.0.0")
public interface ParameterParser<S extends CommandSender, V> {

    /**
     * See {@link ParameterParser}.
     */
    @Nullable V parse(S sender, CommandInput input);
}
