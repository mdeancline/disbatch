package io.github.disbatch.command.parameter.model;

/**
 * @deprecated see {@link PairedParameter}
 */
@Deprecated
public class PairedArgument<F, L> {
    private final F first;
    private final L last;

    PairedArgument(final F first, final L last) {
        this.first = first;
        this.last = last;
    }

    public F getFirst() {
        return first;
    }

    public L getLast() {
        return last;
    }
}
