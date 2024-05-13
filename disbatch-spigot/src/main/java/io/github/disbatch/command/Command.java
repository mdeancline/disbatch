package io.github.disbatch.command;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.Disbatch;
import io.github.disbatch.command.builder.CommandBuilder;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Represents an executable command within a Minecraft server running a Spigot implementation, which acts based on
 * various user inputs.
 *
 * @param <S> any type extending {@link CommandSender} that can safely execute the {@code Command}.
 * @apiNote Not to be confused with {@link org.bukkit.command.Command}.
 * @see Disbatch#register(Command, CommandDescriptor)
 * @see Disbatch#register(Command, CommandDescriptor, JavaPlugin)
 * @see CommandBuilder
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
     * @return a list of tab completions for the specified arguments, which may be empty or immutable.
     */
    default List<String> tabComplete(final S sender, final CommandInput input) {
        return ImmutableList.of();
    }
}
