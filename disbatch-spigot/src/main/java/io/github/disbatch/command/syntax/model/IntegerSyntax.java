package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates an {@link Integer} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [integer number]
 *
 * @since 1.1.0
 */
public final class IntegerSyntax extends NumericSyntax<CommandSender, Integer> {
    private final int min;
    private final int max;

    /**
     * Constructs a new {@code IntegerSyntax} with the specified argument label.
     */
    public IntegerSyntax(@NotNull final String label) {
        this(label, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Constructs a new {@code IntegerSyntax} with the specified argument label, minimum, and maximum values.
     */
    public IntegerSyntax(@NotNull final String label, final int min, final int max) {
        super(label);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        if (isInteger(binding.getArgument())) {
            final float value = parseFloat(binding.getArgument());
            return value >= min && value <= max;
        }

        return false;
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    @Override
    public @Nullable Integer parse(final CommandSender sender, final CommandInput input) {
        try {
            return parseInt(input.getArgument(0));
        } catch (final NumberFormatException ignored) {
            return null;
        }
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
