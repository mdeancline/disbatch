package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Serves the same purpose and functionality as {@link Parameter#parse(CommandSender, CommandInput)}.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface ParameterParser<S extends CommandSender, V> {

    /**
     * See {@link ParameterParser}.
     */
    @Nullable V parse(S sender, CommandInput input);
}
