package io.github.disbatch.command.parameter;

import org.jetbrains.annotations.ApiStatus;

/**
 * A namespace for {@link ParameterUsage} convenience and utility methods.
 */
@ApiStatus.AvailableSince("1.0.0")
public final class ParameterUsages {
    private ParameterUsages() {
        throw new AssertionError();
    }

    public static ParameterUsage withChevrons(final String baseMessage, final String... usageLabels) {
        return new ParameterUsage.Builder()
                .baseMessage(baseMessage)
                .labelHead('<')
                .labelTail('>')
                .usageLabels(usageLabels).build();
    }

    public static ParameterUsage withCurlyBraces(final String baseMessage, final String... usageLabels) {
        return new ParameterUsage.Builder()
                .baseMessage(baseMessage)
                .labelHead('{')
                .labelTail('}')
                .usageLabels(usageLabels).build();
    }

    public static ParameterUsage withSquareBraces(final String baseMessage, final String... usageLabels) {
        return new ParameterUsage.Builder()
                .baseMessage(baseMessage)
                .labelHead('[')
                .labelTail(']')
                .usageLabels(usageLabels).build();
    }
}
