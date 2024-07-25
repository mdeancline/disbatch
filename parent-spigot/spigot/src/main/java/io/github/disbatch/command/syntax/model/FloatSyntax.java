package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputBinding;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Float} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [decimal number]
 *
 * @since 1.1.0
 */
public final class FloatSyntax extends NumericSyntax<CommandSender, Float> {
    private final float min;
    private final float max;

    /**
     * Constructs a new {@code FloatSyntax} with the specified argument label.
     */
    public FloatSyntax(@NotNull final String label) {
        this(label, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    /**
     * Constructs a new {@code FloatSyntax} with the specified argument label, minimum, and maximum values.
     */
    public FloatSyntax(@NotNull final String label, final float min, final float max) {
        super(label);
        this.min = min;
        this.max = max;
    }


    @Override
    public @Nullable Float parse(final CommandSender sender, final CommandInput input) {
        try {
            return parseFloat(input.getArgument(0));
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public boolean matches(final CommandInputBinding binding) {
        if (isFloating(binding.getArgument())) {
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
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }
}
