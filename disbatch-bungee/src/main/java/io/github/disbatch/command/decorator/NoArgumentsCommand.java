package io.github.disbatch.command.decorator;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandExecutor;
import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link CommandProxy} for handling cases where a {@link CommandSender} passes no arguments.
 *
 * @param <S> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class NoArgumentsCommand<S extends CommandSender> extends CommandProxy<S> {
    private final CommandExecutor<S> noArgsExecutor;

    public NoArgumentsCommand(final Command<S> innerCommand, final CommandExecutor<S> noArgsExecutor) {
        super(innerCommand);
        this.noArgsExecutor = noArgsExecutor;
    }

    @Override
    public void execute(final S sender, final @NotNull CommandInput input) {
        if (input.getArgumentLength() == 0) noArgsExecutor.execute(sender, input);
        else super.execute(sender, input);
    }
}
