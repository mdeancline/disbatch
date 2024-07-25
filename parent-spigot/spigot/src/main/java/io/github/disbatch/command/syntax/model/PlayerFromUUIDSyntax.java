package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputBinding;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses an online {@link Player} by their {@link UUID} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [uuid]
 *
 * @since 1.1.0
 */
public final class PlayerFromUUIDSyntax extends UUIDOrientedSyntax<CommandSender, Player> {

    /**
     * Constructs a new {@code PlayerFromUUIDSyntax} with the specified argument label.
     */
    public PlayerFromUUIDSyntax(@NotNull final String label) {
        super(label);
    }

    @Override
    public @Nullable Player parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);

        return isUniqueId(arg)
                ? Bukkit.getPlayer(UUID.fromString(arg))
                : null;
    }

    @Override
    public boolean matches(final CommandInputBinding binding) {
        final String argument = binding.getArgument();
        return isUniqueId(argument) && Bukkit.getPlayer(UUID.fromString(argument)) != null;
    }

    @Override
    public boolean isGreedy() {
        return false;
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