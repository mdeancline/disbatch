package io.github.disbatch.command.syntax;

import org.jetbrains.annotations.NotNull;

public final class SimpleLiteral implements CommandSyntax.Literal {
    private final String label;
    private final boolean greedy;

    public SimpleLiteral(@NotNull final String label, final boolean greedy) {
        this.label = label;
        this.greedy = greedy;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isGreedy() {
        return greedy;
    }
}
