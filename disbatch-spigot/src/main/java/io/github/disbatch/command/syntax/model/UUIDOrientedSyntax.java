package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.syntax.AbstractSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A {@code CommandSyntax} abstraction holding the functionalities necessary to create or retrieve a {@link UUID} based
 * on a parsable, passed argument.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @since 1.1.0
 */
public abstract class UUIDOrientedSyntax<S extends CommandSender, V> extends AbstractSyntax<S, V> {
    private static final String UUID_REGEX = "[\\da-fA-F]{8}-[\\da-fA-F]{4}-[34][\\da-fA-F]{3}-[89ab][\\da-fA-F]{3}-[\\da-fA-F]{12}";

    protected UUIDOrientedSyntax(final @NotNull String... labels) {
        super(labels);
    }

    /**
     * @param argument
     * @return
     */
    protected final boolean isUniqueId(final String argument) {
        return argument.matches(UUID_REGEX);
    }
}
