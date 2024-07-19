package io.github.disbatch.command.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class SimpleLiteral implements CommandSyntax.Literal {
    private final Map<String, CommandSyntax.Literal> children = new HashMap<>();
    private SimpleLiteral parent;
    private SimpleLiteral next;
    private final String value;

    public SimpleLiteral(final @NotNull String value) {
        this.value = value;
    }

    @Override
    public String getLabel() {
        return value;
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    public void setParent(final @NotNull SimpleLiteral parent) {
        if (this.parent != null)
            throw new IllegalStateException("Parent has already been set");

        this.parent = parent;
    }

    public void addChild(final @NotNull SimpleLiteral node) {
        children.put(node.getLabel(), node);
        node.setParent(this);
    }
}
