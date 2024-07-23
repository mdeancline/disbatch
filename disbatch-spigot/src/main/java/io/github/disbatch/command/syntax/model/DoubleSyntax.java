package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Double} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [decimal number]
 *
 * @since 1.1.0
 */
public final class DoubleSyntax extends NumericSyntax<CommandSender, Double> {
    private final double min;
    private final double max;

    /**
     * Constructs a new {@code DoubleSyntax} with the specified argument label.
     */
    public DoubleSyntax(@NotNull final String label) {
        this(label, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Constructs a new {@code DoubleSyntax} with the specified argument label, minimum, and maximum values.
     */
    public DoubleSyntax(@NotNull final String label, final double min, final double max) {
        super(label);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        if (isFloating(binding.getArgument())) {
            final double value = parseDouble(binding.getArgument());
            return value >= min && value <= max;
        }

        return false;
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    @Override
    public @Nullable Double parse(final CommandSender sender, final CommandInput input) {
        try {
            return parseDouble(input.getArgument(0));
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
