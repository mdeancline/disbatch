package io.github.disbatch.mock;

import io.github.disbatch.CommandRegistrar;
import io.github.disbatch.CommandRegistrars;
import io.github.disbatch.InvitePlayersCommand;
import io.github.disbatch.TeleportCommand;
import io.github.disbatch.command.CommandGroup;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.parameter.ParameterUsage;
import org.bukkit.plugin.java.JavaPlugin;

public class DisbatchPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(this);

        registrar.register(new PlayerCommand(), new CommandDescriptor.Builder()
                .label("player")
                .topic(new PlayerCommandTopic())
                .build());

        registrar.register(new TeleportCommand(), "teleport");
        registrar.register(new InvitePlayersCommand(), "invite");

        final CommandGroup<?> entityTypeCmdGroup = new CommandGroup<>(new ParameterUsage.Builder()
                .usageLabels("enum sensitivity", "entity name")
                .build())
                .withCommand(new EntityTypeCaseInsensitiveCommand(new ParameterUsage.Builder()
                        .usageLabels("name")
                        .build()), "insensitive")
                .withCommand(new EntityTypeCaseSensitiveCommand(new ParameterUsage.Builder()
                        .usageLabels("name")
                        .build()), "sensitive");

        registrar.register(entityTypeCmdGroup, "entitytype");
        registrar.register(new InvitePlayersCommand(), "invite");
    }
}
