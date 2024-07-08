package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.InvalidInputHandler;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.PairedArgument;
import io.github.disbatch.command.parameter.model.PairedParameter;
import io.github.disbatch.command.parameter.model.PlayerFromNameParameter;
import io.github.disbatch.command.parameter.model.StringParameter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class BanCommand extends ParameterizedCommand<CommandSender, PairedArgument<Player, String>> {
    protected BanCommand(final @NotNull InvalidInputHandler<CommandSender> handler) {
        super(new PairedParameter<>(new PlayerFromNameParameter(), new StringParameter()), handler);
    }

    @Override
    protected void execute(final CommandSender sender, final PairedArgument<Player, String> argument, final CommandInput input) {
        final Player player = argument.getFirst();
        final String reason = argument.getLast();

        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, new Date(), sender.getName());
    }
}
