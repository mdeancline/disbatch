package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses an {@link OfflinePlayer} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [uuid]
 *
 * @since 1.1.0
 */
public final class OfflinePlayerSyntax extends UUIDOrientedSyntax<CommandSender, OfflinePlayer> {

    /**
     * Constructs a new {@code OfflinePlayerSyntax} with the specified argument label.
     */
    public OfflinePlayerSyntax(final @NotNull String label) {
        super(label);
    }

    @Override
    public @Nullable OfflinePlayer parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);

        return isUniqueId(arg)
                ? Bukkit.getOfflinePlayer(UUID.fromString(arg))
                : null;
    }

    @Override
    public boolean matches(CommandInput.Binding binding) {
        final String argument = binding.getArgument();
        return isUniqueId(argument) && Bukkit.getOfflinePlayer(UUID.fromString(argument)) != null;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }

    @Override
    protected boolean isGreedy() {
        return false;
    }
}
