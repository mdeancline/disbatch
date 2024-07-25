package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;

class CommandDispatcherContext_1_13_R1 implements CommandDispatcherContext {
    @SuppressWarnings("unchecked")
    @Override
    public CommandDispatcher<Sender> getDispatcher() {
        final CommandDispatcher<?> dispatcher = ((CraftServer) Bukkit.getServer()).getServer().getCommandDispatcher().a();
        return (CommandDispatcher<Sender>) dispatcher;
    }
}
