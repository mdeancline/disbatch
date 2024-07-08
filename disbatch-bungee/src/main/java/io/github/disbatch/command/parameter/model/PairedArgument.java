package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.parameter.ParameterizedCommand;

/**
 * Acts as a holder of the {@code Object} arguments created from any two {@code Parameter}s of a {@link PairedParameter}
 * when a {@link ParameterizedCommand} is executed.
 *
 * @param <F> type of the first {@code Object} argument
 * @param <L> type of the last {@code Object} argument
 *
 * @since 1.0.0
 */
public final class PairedArgument<F, L> {
    private final F first;
    private final L last;

    PairedArgument(final F first, final L last) {
        this.first = first;
        this.last = last;
    }

    /**
     * Retrieves the first {@code Object} argument.
     *
     * @return the first {@code Object} argument
     */
    public F getFirst() {
        return first;
    }

    /**
     * Retrieves the last {@code Object} argument.
     *
     * @return the last {@code Object} argument
     */
    public L getLast() {
        return last;
    }
}
