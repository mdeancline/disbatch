package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link World} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [world name]
 *
 * @since 1.0.0
 */
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
