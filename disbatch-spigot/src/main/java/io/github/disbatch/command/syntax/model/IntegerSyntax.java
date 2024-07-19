package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO create minimum and maximum integer input requirements
/**
 * Creates an {@link Integer} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [integer number]
 *
 * @since 1.1.0
 */
public final class IntegerSyntax extends NumericSyntax<CommandSender, Integer> {

    /**
     * Constructs a new {@code IntegerSyntax} with the specified argument label.
     */
    public IntegerSyntax(final @NotNull String label) {
        super(label);
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return isInteger(binding.getArgument());
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
