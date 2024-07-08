package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Vector} based on parsable, passed arguments.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [x] [y] [z]
 *
 * @since 1.0.0
 */
public final class VectorParameter extends NumericParameter<CommandSender, Vector> {
    
    @Override
    public @Nullable Vector parse(final CommandSender sender, final CommandInput input) {
        return isDoubleParsable(input)
                ? new Vector(
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
