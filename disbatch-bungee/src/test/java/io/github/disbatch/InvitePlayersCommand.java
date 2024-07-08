package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterUsage;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.model.ArrayParameter;
import io.github.disbatch.command.parameter.model.PlayerFromNameParameter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class InvitePlayersCommand extends ParameterizedCommand<ProxiedPlayer, ProxiedPlayer[]> {
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
    protected void execute(final ProxiedPlayer sender, final ProxiedPlayer[] argument, final CommandInput input) {
        for (final ProxiedPlayer player : argument)
            player.sendMessage("You've been invited!");
    }
}
