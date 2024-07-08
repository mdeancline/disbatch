package io.github.disbatch.mock;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.ParameterUsage;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.command.parameter.Suggesters;
import io.github.disbatch.command.parameter.model.enumeration.EnumParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class EntityTypeCaseSensitiveCommand extends ParameterizedCommand<CommandSender, EntityType> {
    public EntityTypeCaseSensitiveCommand(final ParameterUsage usage) {
        super(new EnumParameter<>(EntityType.class)
                .withSuggester(Suggesters.of(EntityType.values())), usage);
    }

    @Override
    protected void execute(final CommandSender sender, final EntityType type, final CommandInput input) {
        System.out.println("EntityType from case sensitive: " + type.name());
    }
}
