package io.github.disbatch.command.syntax.model;

import com.google.common.base.Strings;
import io.github.disbatch.command.CommandInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parses and forms a {@code String} from all passed arguments, joined via single whitespace.
 * <p>
 * <b>Syntax:</b> [string value]
 *
 * @since 1.1.0
 */
public final class StringSyntax extends SenderIndependentSyntax<String> {
    private final int maxCharacters;

    public StringSyntax(final @NotNull String label) {
        this(label, Integer.MAX_VALUE);
    }

    public StringSyntax(final @NotNull String label, final int maxCharacters) {
        super(label);
        this.maxCharacters = maxCharacters;
    }

    @Override
    protected @Nullable String parse(final CommandInput input) {
        return Strings.emptyToNull(input.getArgumentLine());
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return binding.getArgumentLine().length() <= maxCharacters;
    }

    @Override
    public boolean isGreedy() {
        return true;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return Integer.MAX_VALUE;
    }
}
