package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

//TODO fix 1_19_R3 import
class CommandDispatcherContext_1_20_R3 implements CommandDispatcherContext {
    @SuppressWarnings("unchecked")
    @Override
    public CommandDispatcher<Sender> getDispatcher() {
        final CommandDispatcher<?> dispatcher = ((CraftServer) Bukkit.getServer()).getServer().aC().a();
        return (CommandDispatcher<Sender>) dispatcher;
    }
}
