package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Vector} based on parsable, passed arguments.
 * <p>
 * <b>Input syntax:</b> [x] [y] [z]
 *
 * @since 1.1.0
 */
public final class VectorSyntax extends NumericSyntax<CommandSender, Vector> {

    /**
     * Constructs a new {@code VectorSyntax} with the specified argument labels.
     */
    public VectorSyntax(@NotNull final String xLabel, @NotNull final String yLabel, @NotNull final String zLabel) {
        super(xLabel, yLabel, zLabel);
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return isFloating(binding.getArgument());
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    @Override
    public @Nullable Vector parse(final CommandSender sender, final CommandInput input) {
        try {
            return new Vector(
                    parseDouble(input.getArgument(0)),
                    parseDouble(input.getArgument(1)),
                    parseDouble(input.getArgument(2)));
        } catch (final NumberFormatException ignored) {
            return null;
        }
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
