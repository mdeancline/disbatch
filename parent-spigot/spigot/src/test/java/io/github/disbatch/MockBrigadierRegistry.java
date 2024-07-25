package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;

class MockBrigadierRegistry implements BrigadierRegistry {
    private final CommandDispatcher<CommandSender> dispatcher;

    MockBrigadierRegistry(final CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void register(final LiteralArgumentBuilder<CommandSender> builder) {
        dispatcher.register(builder);
    }
}
