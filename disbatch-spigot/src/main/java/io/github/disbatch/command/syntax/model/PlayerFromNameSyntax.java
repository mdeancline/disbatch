package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses an online {@link Player} by their name based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [player name]
 *
 * @since 1.1.0
 */
public final class PlayerFromNameSyntax extends SenderIndependentSyntax<Player> {

    /**
     * Constructs a new {@code PlayerFromNameSyntax} with the specified argument label.
     */
    public PlayerFromNameSyntax(@NotNull final String label) {
        super(label);
    }

    @Override
    public @Nullable Player parse(final CommandInput input) {
        return Bukkit.getPlayer(input.getArgument(0));
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        final Player player = Bukkit.getPlayer(binding.getArgument());
        return player != null && player.isOnline();
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
