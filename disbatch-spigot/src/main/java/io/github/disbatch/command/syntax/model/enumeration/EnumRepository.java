package io.github.disbatch.command.syntax.model.enumeration;

import org.jetbrains.annotations.Nullable;

/**
 * Maintains the storage of valid constants of the enum subtype {@link E}.
 *
 * @param <E> the enum type subclass.
 * @apiNote This should only be used in conjunction with {@link EnumConverter}.
 *
 * @since 1.1.0
 */
public interface EnumRepository<E extends Enum<E>> {

    /**
     * Retrieves an enum from the given argument representing the name of the constant.
     *
     * @param name the given name of the enum.
     * @return the retrieved enum.
     */
    @Nullable E get(String name);
}
