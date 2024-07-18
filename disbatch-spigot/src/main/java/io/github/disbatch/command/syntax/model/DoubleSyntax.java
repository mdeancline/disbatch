package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link Double} based on a parsable, passed argument.
 * <p>
 * <b>Syntax:</b> [decimal number]
 *
 * @since 1.1.0
 */
public final class DoubleSyntax extends NumericSyntax<CommandSender, Double> {

    /**
     * Constructs a new {@code DoubleSyntax} with the specified argument label.
     */
    public DoubleSyntax(final @NotNull String label) {
        super(label);
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
