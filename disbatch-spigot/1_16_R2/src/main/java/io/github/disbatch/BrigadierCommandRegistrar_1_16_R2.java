package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;

class BrigadierCommandRegistrar_1_16_R2 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_16_R2() {
        super();
    }

    @Override
    protected CommandDispatcher<?> getDispatcher() {
        return ((CraftServer) Bukkit.getServer()).getServer().getCommandDispatcher().a();
    }
}
