package io.github.disbatch.mock;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.command.CommandDescriptor;
import io.github.disbatch.command.CommandInputs;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MockCommandRegistrar implements CommandRegistrar {
    private final CommandDispatcher<CommandSender> dispatcher;

    public MockCommandRegistrar(final CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void register(@NotNull String label, @NotNull final CommandDescriptor descriptor) {
        final CommandDescriptor.Executor command = descriptor.getExecutor();
        final CommandSyntax<?, ?> syntax = descriptor.getSyntax();

        dispatcher.register(LiteralArgumentBuilder.<CommandSender>literal(label).executes(context -> {
            final String input = context.getInput();
            final String[] arguments = input.substring(0, input.indexOf(" ")).split(" ");
            command.execute(context.getSource(), CommandInputs.of(label, arguments, syntax));
            return Command.SINGLE_SUCCESS;
        }));
    }

    @Override
    public void registerFromFile(@NotNull String label, @NotNull CommandDescriptor descriptor) {

    }
}
