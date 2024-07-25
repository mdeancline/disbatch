package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.command.CommandSender;

class MockBrigadierRegistryContext implements BrigadierRegistryContext {
    private final CommandDispatcher<CommandSender> dispatcher;

    MockBrigadierRegistryContext(final CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public BrigadierRegistry getRegistry() {
        return new MockBrigadierRegistry(dispatcher);
    }
}
