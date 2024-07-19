package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Location} for the passed {@link Player}'s {@link World} based on parsable, passed arguments.
 * <p>
 * <b>Input syntax:</b> [player name] [x] [y] [z]
 *
 * @since 1.1.0
 */
public final class LocationFromPlayerSyntax extends NumericSyntax<CommandSender, Location> {

    /**
     * Constructs a new {@code LocationFromPlayerSyntax} with the specified argument labels.
     */
    public LocationFromPlayerSyntax(final @NotNull String nameLabel, final @NotNull String xLabel, final @NotNull String yLabel, final @NotNull String zLabel) {
        super(nameLabel, xLabel, yLabel, zLabel);
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        if (binding.getIndex() == 0) {
            final Player player = Bukkit.getPlayer(binding.getLabel());
            return player != null && player.isOnline();
        } else {
            return isNumber(binding.getArgument());
        }
    }

    @Override
    public @Nullable Location parse(final CommandSender sender, final CommandInput input) {
        try {
            final Player player = Bukkit.getPlayer(input.getArgument(0));
            if (player == null || !player.isOnline()) return null;

            return new Location(player.getWorld(),
                    parseDouble(input.getArgument(1)),
                    parseDouble(input.getArgument(2)),
                    parseDouble(input.getArgument(3)));
        } catch (final NumberFormatException ignored) {
            return null;
        }
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
