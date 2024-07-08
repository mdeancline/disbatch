package io.github.disbatch.command.parameter.model;

import com.google.common.base.Strings;
import io.github.disbatch.command.CommandInput;
import org.jetbrains.annotations.Nullable;

/**
 * Parses and forms a {@code String} from all passed arguments, joined via single whitespace.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [string value]
 *
 * @since 1.0.0
 */
public final class StringParameter extends SenderIndependentParameter<String> {
    private final int maxUsage;

    public StringParameter() {
        this(Integer.MAX_VALUE);
    }

    public StringParameter(final int maxUsage) {
        this.maxUsage = maxUsage;
    }

    @Override
    protected @Nullable String parse(final CommandInput input) {
        return Strings.emptyToNull(input.getArgumentLine());
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return maxUsage;
    }
}
