package io.github.disbatch.command.builder;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Serves as a flexible solution for creating a new {@link Command} without defining an anonymous or explicit abstraction.
 *
 * @param <S> any type extending {@link CommandSender} that can safely execute any built {@link Command}.
 */
@ApiStatus.AvailableSince("1.0")
public final class CommandBuilder<S extends CommandSender> {
    private CommandExecutor<S> executor;
    private TabCompleter<S> tabCompleter = TabCompleters.empty();

    public CommandBuilder<S> executor(final @NotNull CommandExecutor<S> executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder<S> tabCompleter(final @NotNull TabCompleter<S> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    /**
     * Creates a new {@link Command}.
     *
     * @return the created {@code Command}.
     */
    public Command<S> build() {
        return new BuiltCommand<>(executor, tabCompleter);
    }

    private static class BuiltCommand<S extends CommandSender> implements Command<S> {
        private final CommandExecutor<S> executor;
        private final TabCompleter<S> tabCompleter;

        private BuiltCommand(final @NotNull CommandExecutor<S> executor, final @NotNull TabCompleter<S> tabCompleter) {
            this.executor = executor;
            this.tabCompleter = tabCompleter;
        }

        @Override
        public void execute(final S sender, final CommandInput input) {
            executor.execute(sender, input);
        }

        @Override
        public List<String> tabComplete(final S sender, final CommandInput input) {
            return tabCompleter.tabComplete(sender, input);
        }
    }
}
