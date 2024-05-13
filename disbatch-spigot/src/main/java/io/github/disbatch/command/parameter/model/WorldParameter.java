package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link World} based on a parsable, passed argument.
 */
@ApiStatus.AvailableSince("1.0")
public final class WorldParameter extends SenderIndependentParameter<World> {
    
    @Override
    protected @Nullable World parse(final CommandInput input) {
        return Bukkit.getWorld(input.getArgument(0));
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
