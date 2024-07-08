package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Creates an {@link Integer} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [whole number]
 *
 * @since 1.0.0
 */
public final class IntegerParameter extends NumericParameter<CommandSender, Integer> {
    
    @Override
    public @Nullable Integer parse(final CommandSender sender, final CommandInput input) {
        return isIntegerParsable(input)
                ? parseInt(input.getArgument(0))
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
