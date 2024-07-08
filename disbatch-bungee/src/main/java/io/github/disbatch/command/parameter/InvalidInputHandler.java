package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;

/**
 * Handles situations where a {@link ParameterizedCommand} is unable to parse a {@link CommandInput}.
 * @param <S> the {@link CommandSender} type responsible for executing the originating {@code ParameterizedCommand}.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface InvalidInputHandler<S extends CommandSender> {

    /**
     * @param sender
     * @param input
     */
    void handle(S sender, InvalidInput input);
}
