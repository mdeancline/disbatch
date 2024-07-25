package io.github.disbatch;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.command.CommandSender;

interface CommandDispatcherContext {
    CommandDispatcher<Sender> getDispatcher();

    interface Sender {
        CommandSender getBukkitSender();
    }
}
