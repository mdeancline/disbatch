package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.LocationByEntityParameter;
import io.github.disbatch.command.parameter.model.PlayerFromUUIDParameter;
import io.github.disbatch.command.parameter.model.paired.PairedArgument;
import io.github.disbatch.command.parameter.model.paired.PairedParameter;
import io.github.disbatch.command.parameter.usage.ParameterUsage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCommand extends ParameterizedCommand<Player, PairedArgument<Player, Location>> {
    public TeleportCommand() {
        super(new PairedParameter<>(new PlayerFromUUIDParameter(), new LocationByEntityParameter<>()),
                ParameterUsage.builder().build());
    }

    @Override
    protected void execute(final Player sender, final PairedArgument<Player, Location> argument, final CommandInput input) {
        argument.getFirst().teleport(argument.getLast());
    }
}
