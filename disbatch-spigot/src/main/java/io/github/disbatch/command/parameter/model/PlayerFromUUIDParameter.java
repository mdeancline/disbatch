package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses an online {@link Player} by their {@link UUID} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [uuid]
 *
 * @since 1.0.0
 */
public final class PlayerFromUUIDParameter extends UUIDOrientedParameter<CommandSender, Player> {
    
    @Override
    public @Nullable Player parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);

        return isUniqueId(arg)
                ? Bukkit.getPlayer(UUID.fromString(arg))
                : null;
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
