package io.github.disbatch.command.parameter.model.enumeration;

import org.jetbrains.annotations.Nullable;

/**
 * Maintains the storage of valid constants of the enum subtype {@link E}.
 *
 * @param <E> the enum type subclass.
 * @apiNote This should only be used in conjunction with {@link EnumConverter}.
 *
 * @since 1.0.0
 */
public interface EnumRepository<E extends Enum<E>> {

    /**
     * Retrieves an enum from the given argument representing the name of the constant.
     *
     * @param nameArgument the given argument.
     * @return the retrieved enum.
     */
    @Nullable E get(String nameArgument);
}
