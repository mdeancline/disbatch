package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Parses an online {@link Player} by their name based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [player name]
 *
 * @since 1.0.0
 */
public final class PlayerFromNameParameter extends SenderIndependentParameter<Player> {
    
    @Override
    protected @Nullable Player parse(final CommandInput input) {
        return Bukkit.getPlayer(input.getArgument(0));
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
