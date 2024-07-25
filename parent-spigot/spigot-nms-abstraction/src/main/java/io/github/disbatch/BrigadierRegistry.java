package io.github.disbatch;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;

interface BrigadierRegistry {
    void register(LiteralArgumentBuilder<CommandSender> builder);
}
