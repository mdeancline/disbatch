package io.github.disbatch.mock;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements Command<ProxiedPlayer> {
    @Override
    public void execute(final ProxiedPlayer sender, final @NotNull CommandInput input) {
        sender.sendMessage("You executed this command, " + sender.getName());
    }
}
