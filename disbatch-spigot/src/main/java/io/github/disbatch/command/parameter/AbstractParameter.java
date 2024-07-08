package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A {@code Parameter} abstraction that utilizes its own {@link Suggester}, which is empty by default, to return a list of
 * possible suggestions.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @see #withSuggester(Suggester)
 *
 * @since 1.0.0
 */
public abstract class AbstractParameter<S extends CommandSender, V> implements Parameter<S, V> {
    private Suggester<S> suggester = Suggesters.empty();

    @Override
    public final Collection<String> getSuggestions(final S sender, final CommandInput input) {
        return suggester.getSuggestions(sender, input);
    }

    /**
     * Sets the {@link Suggester} to be used for the {@code AbstractParameter}.
     *
     * @param suggester the suggester to be used.
     * @return this {@code AbstractParameter} as a {@code Parameter}.
     */
    public final Parameter<S, V> withSuggester(final @NotNull Suggester<S> suggester) {
        this.suggester = suggester;
        return this;
    }
}
