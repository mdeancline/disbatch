package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.syntax.AbstractSyntax;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code CommandSyntax} abstraction that doesn't rely on the {@link CommandSender} to create or retrieve an {@code Object}
 * based on parsable, passed arguments.
 *
 * @param <V> the type from the resulting {@code Object} parsed from arguments.
 * @since 1.1.0
 */
public abstract class SenderIndependentSyntax<V> extends AbstractSyntax<CommandSender, V> {

    protected SenderIndependentSyntax(@NotNull final String... labels) {
        super(labels);
    }

    @Override
    public final @Nullable V parse(final CommandSender sender, final CommandInput input) {
        return parse(input);
    }

    /**
     * Serves the same functionality as {@link CommandSyntax#parse(CommandSender, CommandInput)} but without the
     * {@link CommandSender}.
     *
     * @param input the {@link CommandInput} passed from a command.
     * @return the parsed {@code Object} result.
     */
    protected abstract @Nullable V parse(CommandInput input);
}
