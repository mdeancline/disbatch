package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for the {@link Entity} sender's {@link World} based on parsable, passed arguments.
 *
 * @param <S> the {@code Entity} type to create a {@code Location} from
 */
@ApiStatus.AvailableSince("1.0")
public final class LocationByEntityParameter<S extends Entity> extends NumericParameter<S, Location> {

    @Override
    public @Nullable Location parse(final S sender, final CommandInput input) {
        return isDoubleParsable(input)
                ? new Location(sender.getWorld(),
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
