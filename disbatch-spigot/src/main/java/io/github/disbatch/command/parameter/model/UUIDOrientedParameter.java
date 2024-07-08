package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.parameter.AbstractParameter;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * A {@code Parameter} abstraction holding the functionalities necessary to create or retrieve a {@link UUID} based on a parsable,
 * passed argument.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 *
 * @since 1.0.0
 */
public abstract class UUIDOrientedParameter<S extends CommandSender, V> extends AbstractParameter<S, V> {
    private static final String UUID_REGEX = "[\\da-fA-F]{8}-[\\da-fA-F]{4}-[34][\\da-fA-F]{3}-[89ab][\\da-fA-F]{3}-[\\da-fA-F]{12}";

    protected UUIDOrientedParameter() {
    }

    /**
     * @param argument
     * @return
     */
    protected final boolean isUniqueId(final String argument) {
        return argument.matches(UUID_REGEX);
    }
}
