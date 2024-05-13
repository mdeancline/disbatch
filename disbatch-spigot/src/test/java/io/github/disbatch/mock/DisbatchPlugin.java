package io.github.disbatch.mock;

import io.github.disbatch.Disbatch;
import io.github.disbatch.InvitePlayersCommand;
import io.github.disbatch.TeleportCommand;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.group.CommandGroup;
import io.github.disbatch.command.parameter.usage.ParameterUsage;
import org.bukkit.plugin.java.JavaPlugin;

public class DisbatchPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Disbatch.register(new PlayerCommand(), CommandDescriptor.builder()
                .label("player")
                .topic(new PlayerCommandTopic())
                .build());

        Disbatch.register(new TeleportCommand(), CommandDescriptor.label("teleport"));
        Disbatch.register(new InvitePlayersCommand(), CommandDescriptor.label("invite"));

        final CommandGroup<?> entityTypeCmdGroup = new CommandGroup<>(ParameterUsage.builder()
                .usageLabels("conversion")
                .build());
        entityTypeCmdGroup.addCommand(new EntityTypeCaseInsensitiveCommand(ParameterUsage.builder()
                .usageLabels("name")
                .build()), CommandDescriptor.label("insensitive"));
        entityTypeCmdGroup.addCommand(new EntityTypeCaseSensitiveCommand(ParameterUsage.builder()
                .usageLabels("name")
                .build()), CommandDescriptor.label("sensitive"));
        Disbatch.register(entityTypeCmdGroup, CommandDescriptor.label("entitytype"));
        Disbatch.register(entityTypeCmdGroup, CommandDescriptor.label("invite"));
    }
}
