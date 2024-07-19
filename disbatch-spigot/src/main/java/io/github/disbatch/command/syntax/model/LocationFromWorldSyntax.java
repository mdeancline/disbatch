package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for a passed {@link World} based on parsable, passed arguments.
 * <p>
 * <b>Input syntax:</b> [world name] [x] [y] [z]
 *
 * @since 1.1.0
 */
public final class LocationFromWorldSyntax extends NumericSyntax<CommandSender, Location> {

    /**
     * Constructs a new {@code LocationFromWorldSyntax} with the specified argument labels.
     */
    public LocationFromWorldSyntax(final @NotNull String nameLabel, final @NotNull String xLabel, final @NotNull String yLabel, final @NotNull String zLabel) {
        super(nameLabel, xLabel, yLabel, zLabel);
    }

    @Override
    public @Nullable Location parse(final CommandSender sender, final CommandInput input) {
        try {
            final World world = Bukkit.getWorld(input.getArgument(0));
            if (world == null) return null;

            return new Location(world,
                    parseDouble(input.getArgument(1)),
                    parseDouble(input.getArgument(2)),
                    parseDouble(input.getArgument(3)));
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return binding.getIndex() == 0
                ? (Bukkit.getWorld(binding.getLabel()) != null)
                : isNumber(binding.getArgument());
    }

    @Override
    public boolean isGreedy() {
        return false;
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
