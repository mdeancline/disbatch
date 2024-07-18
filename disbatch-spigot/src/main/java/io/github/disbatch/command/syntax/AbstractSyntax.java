package io.github.disbatch.command.syntax;

import io.github.disbatch.command.Suggester;
import io.github.disbatch.command.Suggesters;
import io.github.disbatch.command.Suggestion;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

/**
 * A {@code CommandSyntax} abstraction that utilizes its own {@link Suggester}, which is empty by default, to return a
 * list of possible suggestions.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @see #withSuggester(Suggester)
 *
 * @since 1.1.0
 */
public abstract class AbstractSyntax<S extends CommandSender, V> implements CommandSyntax<S, V> {
    private Suggester<S> suggester = Suggesters.empty();
    private final SimpleNode root;

    protected AbstractSyntax(final String... labels) {
        if (labels.length == 0)
            throw new IllegalArgumentException("There must be at least one label");

        SimpleNode node = new SimpleNode(labels[0]);
        for (int i = 1; i < labels.length; i++) {
            final SimpleNode previous = node;
            node = new SimpleNode(labels[i]);
            previous.addChild(node);
        }

        this.root = node;
    }

    @Override
    public Collection<Suggestion> getSuggestions(S sender, String[] arguments) {
        return suggester.getSuggestions(sender, arguments);
    }

    @Override
    public final @Nullable Node getNode(final int argumentIndex) {
        Node current = root;
        int i = 0;
        final CommandSyntaxNodeIterator iterator = new CommandSyntaxNodeIterator(current, false);

        for (; i <= argumentIndex; i++) {
            if (iterator.hasNext()) current = iterator.next();
            else break;
        }

        return !isGreedy() && i > argumentIndex ? null : current;
    }

    @NotNull
    @Override
    public Iterator<Node> iterator() {
        return new CommandSyntaxNodeIterator(root, false);
    }

    /**
     * Sets the {@link Suggester} to be used for the {@code AbstractParameter}.
     *
     * @param suggester the tab completer to be used.
     * @return this {@code AbstractParameter} as a {@code Parameter}.
     */
    public final CommandSyntax<S, V> withSuggester(final @NotNull Suggester<S> suggester) {
        this.suggester = suggester;
        return this;
    }

    /**
     * Determines if this syntax either covers all arguments in one or only targets individual arguments.
     *
     * @return true if the syntax is greedy, false otherwise
     */
    protected abstract boolean isGreedy();
}
