package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses a {@link UUID} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [uuid]
 *
 * @since 1.0.0
 */
public final class UUIDParameter extends UUIDOrientedParameter<CommandSender, UUID> {
    
    @Override
    public @Nullable UUID parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);
        return isUniqueId(arg) ? UUID.fromString(arg) : null;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }
}
