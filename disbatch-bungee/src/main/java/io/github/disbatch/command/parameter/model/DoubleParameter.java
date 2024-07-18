package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Double} based on a parsable, passed argument.
 * <p>
 * <p>
 * <b>Argument Syntax:</b> [decimal number]
 *
 * @since 1.0.0
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
