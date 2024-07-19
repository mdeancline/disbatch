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
    private final SimpleLiteral root;

    protected AbstractSyntax(final @NotNull String... labels) {
        if (labels.length == 0)
            throw new IllegalArgumentException("There must be at least one label");

        SimpleLiteral node = new SimpleLiteral(labels[0]);
        for (int i = 1; i < labels.length; i++) {
            final SimpleLiteral previous = node;
            node = new SimpleLiteral(labels[i]);
            previous.addChild(node);
        }

        this.root = node;
    }

    @Override
    public Collection<Suggestion> getSuggestions(final S sender, final String[] arguments) {
        return suggester.getSuggestions(sender, arguments);
    }

    @Override
    public final @Nullable Literal getLiteral(final int index) {
        Literal current = root;
        int i = 0;
        final CommandLiteralIterator iterator = new CommandLiteralIterator(current, false);

        for (; i <= index; i++) {
            if (iterator.hasNext()) current = iterator.next();
            else break;
        }

        return !isGreedy() && i > index ? null : current;
    }

    @NotNull
    @Override
    public Iterator<Literal> iterator() {
        return new CommandLiteralIterator(root, false);
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
     * Checks if this syntax is greedy as a whole, meaning it consumes all remaining arguments.
     *
     * @return true if the syntax is greedy, false otherwise
     */
    protected abstract boolean isGreedy();
}
