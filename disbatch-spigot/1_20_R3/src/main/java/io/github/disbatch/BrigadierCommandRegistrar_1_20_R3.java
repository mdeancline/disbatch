package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

class BrigadierCommandRegistrar_1_20_R3 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_20_R3(final Server server) {
        super(server);
    }

    @Override
    protected CommandDispatcher<?> getDispatcher(final Server server) {
        return ((CraftServer) server).getServer().aC().a();
    }
}
