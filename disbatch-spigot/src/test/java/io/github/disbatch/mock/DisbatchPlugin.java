package io.github.disbatch.mock;

import io.github.disbatch.Disbatch;
import io.github.disbatch.InvitePlayersCommand;
import io.github.disbatch.TeleportCommand;
import io.github.disbatch.command.CommandGroup;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.parameter.ParameterUsage;
import org.bukkit.plugin.java.JavaPlugin;

public class DisbatchPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Disbatch.register(new PlayerCommand(), new CommandDescriptor.Builder()
                .label("player")
                .topic(new PlayerCommandTopic())
                .build());

        Disbatch.register(new TeleportCommand(), "teleport");
        Disbatch.register(new InvitePlayersCommand(), "invite");

        final CommandGroup<?> entityTypeCmdGroup = new CommandGroup<>(new ParameterUsage.Builder()
                .usageLabels("enum sensitivity", "entity name")
                .build())
                .withCommand(new EntityTypeCaseInsensitiveCommand(new ParameterUsage.Builder()
                        .usageLabels("name")
                        .build()), "insensitive")
                .withCommand(new EntityTypeCaseSensitiveCommand(new ParameterUsage.Builder()
                        .usageLabels("name")
                        .build()), "sensitive");

        Disbatch.register(entityTypeCmdGroup, "entitytype");
        Disbatch.register(new InvitePlayersCommand(), "invite");
    }
}
