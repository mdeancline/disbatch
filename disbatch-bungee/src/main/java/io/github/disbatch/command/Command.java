package io.github.disbatch.command;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents an executable command within a BungeeCord server implementation, which acts based on
 * various user inputs.
 *
 * @param <S> any type extending {@link CommandSender} that can safely execute the {@code Command}.
 *
 * @apiNote Not to be confused with {@link net.md_5.bungee.api.plugin.Command}.
 *
 * @see CommandRegistrar#register(Command, CommandDescriptor)
 * @see Builder
 *
 * @since 1.0.0
 */
public interface Command<S extends CommandSender> {

    /**
     * Executes the {@code Command}.
     *
     * @param sender the {@link CommandSender} responsible for execution.
     * @param input  the {@link CommandInput} used to execute the {@code Command}.
     */
    void execute(S sender, CommandInput input);

    /**
     * Executed on tab completion, returning a {@code List} of argument options the {@link CommandSender} can tab through.
     *
     * @param sender the {@link CommandSender} responsible for initiating a tab completion.
     * @param input  the {@link CommandInput} present from tab completion.
     *
     * @return a list of tab completions for the specified arguments, which may be empty or immutable.
     */
    default Collection<String> tabComplete(final S sender, final CommandInput input) {
        return ImmutableList.of();
    }

    /**
     * Serves as a flexible solution for creating a new {@link Command} without defining an anonymous or explicit abstraction.
     *
     * @param <S> any type extending {@link CommandSender} that can safely execute any built {@link Command}.
     *
     * @since 1.0.0
     */
    final class Builder<S extends CommandSender> {
        private CommandExecutor<S> executor;
        private TabCompleter<S> tabCompleter = TabCompleters.empty();

        public Builder<S> executor(final @NotNull CommandExecutor<S> executor) {
            this.executor = executor;
            return this;
        }

        public Builder<S> tabCompleter(final @NotNull TabCompleter<S> tabCompleter) {
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
            public Collection<String> tabComplete(final S sender, final CommandInput input) {
                return tabCompleter.tabComplete(sender, input);
            }
        }
    }
}
