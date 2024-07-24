package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;

class BrigadierCommandRegistrar_1_19_R2 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_19_R2(final Server server) {
        super(server);
    }

    @Override
    protected CommandDispatcher<?> getDispatcher(final Server server) {
        return ((CraftServer) server).getServer().aB().a();
    }
}
