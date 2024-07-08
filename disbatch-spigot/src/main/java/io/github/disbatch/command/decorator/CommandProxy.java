package io.github.disbatch.command.decorator;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An abstraction for proxying any {@link Command}. Any method calls that should be delegated to the proxied
 * {@code Command} can be done via the {@code super} reference (e.g., to route execution logic, call
 * {@link Command#execute(CommandSender, CommandInput)} on it).
 *
 * @param <S> {@inheritDoc}
 *
 * @since 1.0.0
 */
public abstract class CommandProxy<S extends CommandSender> implements Command<S> {
    private final Command<S> innerCommand;

    protected CommandProxy(final @NotNull Command<S> innerCommand) {
        this.innerCommand = innerCommand;
    }

    @Override
    public void execute(final S sender, final CommandInput input) {
        innerCommand.execute(sender, input);
    }

    @Override
    public List<String> tabComplete(final S sender, final CommandInput input) {
        return innerCommand.tabComplete(sender, input);
    }

    /**
     * Delegates to {@code innerCommand.toString()}.
     */
    @Override
    public String toString() {
        return innerCommand.toString();
    }
}
