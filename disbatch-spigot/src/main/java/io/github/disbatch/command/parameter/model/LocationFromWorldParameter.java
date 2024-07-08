package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for a passed {@link World} based on parsable, passed arguments.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [world name] [x] [y] [z]
 *
 * @since 1.0.0
 */
public final class LocationFromWorldParameter extends NumericParameter<CommandSender, Location> {

    @Override
    public @Nullable Location parse(final CommandSender sender, final CommandInput input) {
        final World world = Bukkit.getWorld(input.getArgument(0));

        return world != null && isDoubleParsable(input, 1)
                ? new Location(world,
                parseDouble(input.getArgument(1)),
                parseDouble(input.getArgument(2)),
                parseDouble(input.getArgument(3)))
                : null;
    }

    @Override
    public int getMinimumUsage() {
        return 4;
    }

    @Override
    public int getMaximumUsage() {
        return 4;
    }
}
