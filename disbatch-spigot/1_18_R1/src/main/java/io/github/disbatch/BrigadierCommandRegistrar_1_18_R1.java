package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;

class BrigadierCommandRegistrar_1_18_R1 extends BrigadierCommandRegistrar {
    BrigadierCommandRegistrar_1_18_R1() {
        super();
    }

    @Override
    protected CommandDispatcher<?> getDispatcher() {
        return ((CraftServer) Bukkit.getServer()).getServer().aA().a();
    }
}
