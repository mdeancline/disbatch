package io.github.disbatch.command.syntax;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Responsible for executing a command and holding its own syntax. This is particularly useful if you wish to create
 * implementations that manipulate other {@link Command}s without having to worry about providing a separate
 * {@link CommandSyntax} upon creation and registration.
 *
 * @since 1.1.0
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 */
public interface CommandSyntaxExecutor<S extends CommandSender, V> extends CommandExecutor<S, V> {

    /**
     * Retrieves the syntax used for parsing command arguments.
     *
     * @return the command syntax
     */
    CommandSyntax<? super S, V> getSyntax();
}
