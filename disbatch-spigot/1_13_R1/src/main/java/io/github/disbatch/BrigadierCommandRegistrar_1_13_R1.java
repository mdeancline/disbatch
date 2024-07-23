package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;

class BrigadierCommandRegistrar_1_13_R1 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_13_R1(final Server server) {
        super(server);
    }

    @Override
    protected CommandDispatcher<?> getDispatcher(final Server server) {
        return ((CraftServer) server).getServer().commandDispatcher.a();
    }
}