package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@code Boolean} based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [true|false]
 *
 * @since 1.1.0
 */
public final class BooleanSyntax extends SenderIndependentSyntax<Boolean> {

    /**
     * Constructs a new {@code BooleanSyntax} with the specified argument label.
     */
    public BooleanSyntax(@NotNull final String label) {
        super(label);
    }

    @Override
    protected @Nullable Boolean parse(final CommandInput input) {
        final String argument = input.getArgument(0);
        return isBoolean(argument) ? Boolean.valueOf(argument) : null;
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return isBoolean(binding.getArgument());
    }

    private boolean isBoolean(final String argument) {
        return argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("false");
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
