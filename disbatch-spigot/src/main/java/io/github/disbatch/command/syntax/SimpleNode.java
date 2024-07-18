package io.github.disbatch.command.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class SimpleNode implements CommandSyntax.Node {
    private final Map<String, CommandSyntax.Node> children = new HashMap<>();
    private SimpleNode parent;
    private SimpleNode next;
    private final String value;

    public SimpleNode(final @NotNull String value) {
        this.value = value;
    }

    @Override
    public boolean hasChild() {
        return children.size() > 0;
    }

    @Override
    public boolean hasChildren() {
        return children.size() > 1;
    }

    @Override
    public CommandSyntax.Node getChild(final String value) {
        return children.get(value);
    }

    @Override
    public CommandSyntax.Node next() {
        return next;
    }

    @Override
    public String getLabel() {
        return value;
    }

    public void setParent(final @NotNull SimpleNode parent) {
        if (this.parent != null)
            throw new IllegalStateException("Parent has already been set");

        this.parent = parent;
    }

    public void addChild(final @NotNull SimpleNode node) {
        children.put(node.getLabel(), node);
        node.setParent(this);
    }

    @NotNull
    @Override
    public Iterator<CommandSyntax.Node> iterator() {
        return children.values().iterator();
    }
}
