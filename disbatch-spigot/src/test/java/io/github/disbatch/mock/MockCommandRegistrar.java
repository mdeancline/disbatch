package io.github.disbatch.mock;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.disbatch.Command;
import io.github.disbatch.CommandRegistrar;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MockCommandRegistrar implements CommandRegistrar {
    private final CommandDispatcher<CommandSender> dispatcher;

    public MockCommandRegistrar(final CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void register(@NotNull final Command command) {
        final String label = command.getLabel();
        final Command.Executable executable = command.getExecutable();

        dispatcher.register(LiteralArgumentBuilder.<CommandSender>literal(label).executes(context -> {
            final String input = context.getInput();
            final String[] arguments = input.substring(0, input.indexOf(" ")).split(" ");
            executable.execute(context.getSource(), label, arguments);
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        }));
    }

    @Override
    public void registerFromFile(@NotNull final Command command) {

    }
}
