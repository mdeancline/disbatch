package io.github.disbatch.mock;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerCommand implements Command<Player> {
    @Override
    public void execute(final Player sender, final @NotNull CommandInput input) {
        sender.sendMessage("You executed this command, " + sender.getName());
    }
}
