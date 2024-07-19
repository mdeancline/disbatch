package io.github.disbatch.command.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SimpleLiteral implements CommandSyntax.Literal {
    private final List<CommandSyntax.Literal> children = new ArrayList<>();
    private final String label;

    public SimpleLiteral(final @NotNull String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    @Override
    public Collection<CommandSyntax.Literal> getChildren() {
        return children;
    }

    public void addChild(final @NotNull SimpleLiteral literal) {
        children.add(literal);
    }
}
