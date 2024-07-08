package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.TabCompleter;
import io.github.disbatch.command.TabCompleters;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A {@code Parameter} abstraction that utilizes its own {@link TabCompleter}, which is empty by default, to return a
 * list of possible suggestions.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @see #withTabCompleter(TabCompleter) 
 *
 * @since 1.0.0
 */
public abstract class AbstractParameter<S extends CommandSender, V> implements Parameter<S, V> {
    private TabCompleter<S> completer = TabCompleters.empty();

    @Override
    public final Collection<String> tabComplete(final S sender, final CommandInput input) {
        return completer.tabComplete(sender, input);
    }

    /**
     * Sets the {@link TabCompleter} to be used for the {@code AbstractParameter}.
     *
     * @param completer the tab completer to be used.
     *
     * @return this {@code AbstractParameter} as a {@code Parameter}.
     *
     
     */
    public final Parameter<S, V> withTabCompleter(final @NotNull TabCompleter<S> completer) {
        this.completer = completer;
        return this;
    }
}
