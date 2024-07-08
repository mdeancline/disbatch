package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses an {@link OfflinePlayer} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [player name]
 *
 * @since 1.0.0
 */
public final class OfflinePlayerParameter extends UUIDOrientedParameter<CommandSender, OfflinePlayer> {
    
    @Override
    public @Nullable OfflinePlayer parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);

        return isUniqueId(arg)
                ? Bukkit.getOfflinePlayer(UUID.fromString(arg))
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
