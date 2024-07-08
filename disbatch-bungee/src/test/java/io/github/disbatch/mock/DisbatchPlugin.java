package io.github.disbatch.mock;

import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.CommandRegistrars;
import io.github.disbatch.InvitePlayersCommand;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import net.md_5.bungee.api.plugin.Plugin;

public class DisbatchPlugin extends Plugin {
    @Override
    public void onEnable() {
        final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(this);

        registrar.register(new PlayerCommand(), new CommandDescriptor.Builder()
                .label("player")
                .build());
        registrar.register(new ConsoleCommand(), new CommandDescriptor.Builder()
                .label("console")
                .build());

        registrar.register(new InvitePlayersCommand(), "invite");
    }
}
