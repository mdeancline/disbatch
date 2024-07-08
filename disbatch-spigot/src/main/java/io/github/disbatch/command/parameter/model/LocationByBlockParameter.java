package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for the {@link BlockCommandSender}'s {@link World} based on parsable, passed arguments.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [x] [y] [z]
 *
 * @since 1.0.0
 */
public final class LocationByBlockParameter extends NumericParameter<BlockCommandSender, Location> {

    @Override
    public @Nullable Location parse(final BlockCommandSender sender, final CommandInput input) {
        return isDoubleParsable(input)
                ? new Location(sender.getBlock().getWorld(),
                parseDouble(input.getArgument(0)),
                parseDouble(input.getArgument(1)),
                parseDouble(input.getArgument(2)))
                : null;
    }

    @Override
    public int getMinimumUsage() {
        return 3;
    }

    @Override
    public int getMaximumUsage() {
        return 3;
    }
}
