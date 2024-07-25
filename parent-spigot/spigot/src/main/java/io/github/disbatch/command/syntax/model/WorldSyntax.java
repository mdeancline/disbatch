package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputBinding;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link World} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [world name]
 *
 * @since 1.1.0
 */
public final class WorldSyntax extends SenderIndependentSyntax<World> {

    /**
     * Constructs a new {@code WorldSyntax} with the specified argument label.
     */
    public WorldSyntax(@NotNull final String label) {
        super(label);
    }

    @Override
    public @Nullable World parse(final CommandInput input) {
        return Bukkit.getWorld(input.getArgument(0));
    }

    @Override
    public boolean matches(final CommandInputBinding binding) {
        return Bukkit.getWorld(binding.getArgument()) != null;
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
