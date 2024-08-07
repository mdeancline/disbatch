package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterUsage;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.ArrayParameter;
import io.github.disbatch.command.parameter.model.PlayerFromNameParameter;
import org.bukkit.entity.Player;

public class InvitePlayersCommand extends ParameterizedCommand<Player, Player[]> {
    public InvitePlayersCommand() {
        super(new ArrayParameter<>(new PlayerFromNameParameter(), 1, 4),
                new ParameterUsage.Builder()
                        .baseMessage("Usage: %usage")
                        .usageLabels("players")
                        .labelHead('<')
                        .labelTail('>')
                        .build());
    }

    @Override
    protected void execute(final Player sender, final Player[] argument, final CommandInput input) {
        for (final Player player : argument)
            player.sendMessage("You've been invited!");
    }
}
