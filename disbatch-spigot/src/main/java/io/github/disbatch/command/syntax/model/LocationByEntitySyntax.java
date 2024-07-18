package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for the {@link Entity} sender's {@link World} based on parsable, passed arguments.
 * <p>
 * <b>Syntax:</b> [x] [y] [z]
 *
 * @param <S> the {@code Entity} type to create a {@code Location} from
 * @since 1.1.0
 */
public final class LocationByEntitySyntax<S extends Entity> extends NumericSyntax<S, Location> {

    /**
     * Constructs a new {@code LocationByEntitySyntax} with the specified argument labels.
     */
    public LocationByEntitySyntax(final @NotNull String xLabel, final @NotNull String yLabel, final @NotNull String zLabel) {
        super(xLabel, yLabel, zLabel);
    }

    @Override
    public @Nullable Location parse(final S sender, final CommandInput input) {
        try {
            return new Location(sender.getWorld(),
                    parseDouble(input.getArgument(0)),
                    parseDouble(input.getArgument(1)),
                    parseDouble(input.getArgument(2)));
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return isNumber(binding.getArgument());
    }

    @Override
    public boolean isGreedy() {
        return false;
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
