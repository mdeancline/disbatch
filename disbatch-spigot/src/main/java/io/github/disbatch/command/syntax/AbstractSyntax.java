package io.github.disbatch.command.syntax;

import io.github.disbatch.command.Suggester;
import io.github.disbatch.command.Suggesters;
import io.github.disbatch.command.Suggestion;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@code CommandSyntax} abstraction that utilizes its own {@link Suggester}, which is empty by default, to return a
 * list of possible suggestions.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @see #withSuggester(Suggester)
 * @since 1.1.0
 */
public abstract class AbstractSyntax<S extends CommandSender, V> implements CommandSyntax<S, V> {
    private final List<Literal> literals;
    private Suggester<S> suggester = Suggesters.empty();

    protected AbstractSyntax(@NotNull final String... labels) {
        if (labels.length == 0)
            throw new IllegalArgumentException("There must be at least one label");

        if (isGreedy()) {
            if (labels.length != 1)
                throw new IllegalArgumentException("Greedy syntax must have exactly one label");

            this.literals = Collections.singletonList(new SimpleLiteral(labels[0], true));
        } else {
            this.literals = new ArrayList<>(labels.length);

            for (final String label : labels)
                literals.add(new SimpleLiteral(label, false));
        }
    }

    @Override
    public Collection<Suggestion> getSuggestions(final S sender, final String[] arguments) {
        return suggester.getSuggestions(sender, arguments);
    }

    @Override
    @Nullable
    public final Literal getLiteral(final int index) {
        final int maxIndex = literals.size() - 1;
        return isGreedy() && index > maxIndex ? literals.get(maxIndex) : literals.get(index);
    }

    @NotNull
    @Override
    public Iterator<Literal> iterator() {
        return literals.iterator();
    }

    /**
     * Sets the {@link Suggester} to be used for the {@code AbstractParameter}.
     *
     * @param suggester the tab completer to be used.
     * @return this {@code AbstractParameter} as a {@code Parameter}.
     */
    public final CommandSyntax<S, V> withSuggester(@NotNull final Suggester<S> suggester) {
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
