package io.github.disbatch.command.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a simple {@link CommandSyntax.Literal} implementation, which can have child literals and be either greedy
 * or non-greedy.
 *
 * @since 1.1.0
 */
public final class SimpleLiteral implements CommandSyntax.Literal {
    private final List<CommandSyntax.Literal> children = new ArrayList<>();
    private final String label;
    private final boolean greedy;

    /**
     * Constructs a new {@code SimpleLiteral} with the specified label.
     *
     * @param label  the label of the literal
     */
    public SimpleLiteral(@NotNull final String label) {
        this(label, false);
    }

    /**
     * Constructs a new {@code SimpleLiteral} with the specified label and greediness.
     *
     * @param label  the label of the literal
     * @param greedy whether the literal is greedy
     */
    public SimpleLiteral(@NotNull final String label, final boolean greedy) {
        this.label = label;
        this.greedy = greedy;
    }

    @Override
    public boolean isGreedy() {
        return greedy;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Collection<CommandSyntax.Literal> getChildren() {
        return children;
    }

    /**
     * Adds a child literal to this literal.
     *
     * @param literal the child literal to be added
     */
    public void addChild(@NotNull final CommandSyntax.Literal literal) {
        children.add(literal);
    }
}
