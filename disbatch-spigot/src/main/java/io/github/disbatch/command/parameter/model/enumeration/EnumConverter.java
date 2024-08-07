package io.github.disbatch.command.parameter.model.enumeration;

import org.jetbrains.annotations.Nullable;

/**
 * Converts a given argument to an enum of a specific type.
 *
 * @param <E> the enum type subclass.
 * @apiNote This should only be used in conjunction with {@link EnumParameter}.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface EnumConverter<E extends Enum<E>> {

    /**
     * @param nameArgument the name of the enum instance.
     * @param directory the {@link EnumRepository} dedicated to the enum type subclass.
     * @return
     */
    @Nullable E convertWith(String nameArgument, EnumRepository<E> directory);
}
