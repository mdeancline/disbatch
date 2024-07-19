package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO create minimum and maximum float input requirements
/**
 * Parses a {@link Float} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [decimal number]
 *
 * @since 1.1.0
 */
public final class FloatSyntax extends NumericSyntax<CommandSender, Float> {

    /**
     * Constructs a new {@code FloatSyntax} with the specified argument label.
     */
    public FloatSyntax(final @NotNull String label) {
        super(label);
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
    public boolean matches(final CommandInput.Binding binding) {
        return isFloating(binding.getArgument());
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
