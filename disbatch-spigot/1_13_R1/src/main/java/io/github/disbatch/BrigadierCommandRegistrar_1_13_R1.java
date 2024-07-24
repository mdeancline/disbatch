package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;

class BrigadierCommandRegistrar_1_13_R1 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_13_R1() {
        super();
    }

    @Override
    protected CommandDispatcher<?> getDispatcher() {
        return ((CraftServer) Bukkit.getServer()).getServer().getCommandDispatcher().a();
    }
}
