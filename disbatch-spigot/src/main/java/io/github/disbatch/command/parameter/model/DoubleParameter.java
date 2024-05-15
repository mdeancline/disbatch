package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses a {@link Double} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [decimal number]
 */
public final class DoubleParameter extends NumericParameter<CommandSender, Double> {
    
    @Override
    public @Nullable Double parse(final CommandSender sender, final CommandInput input) {
        return isDoubleParsable(input)
                ? parseDouble(input.getArgument(0))
                : null;
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
