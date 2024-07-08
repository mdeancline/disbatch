package io.github.disbatch.command.parameter.model.enumeration;

import java.util.Locale;

/**
 * A namespace for {@link EnumConverter} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class EnumConverters {
    private static EnumConverter<?> LOWERCASE_INSENSITIVE;
    private static EnumConverter<?> UPPERCASE_INSENSITIVE;
    private static EnumConverter<?> CASE_SENSITIVE;

    private EnumConverters() {
        throw new AssertionError();
    }

    /**
     * Retrieves an {@link EnumConverter} accepting arguments that will be treated as all lowercase.
     *
     * @param <E> the enum type subclass.
     * @return the retrieved {@link EnumConverter}.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumConverter<E> lowerCaseInsensitive() {
        return LOWERCASE_INSENSITIVE == null
                ? (EnumConverter<E>) (LOWERCASE_INSENSITIVE = (EnumConverter<E>) (nameArgument, directory) -> directory.get(nameArgument.toLowerCase(Locale.ROOT)))
                : (EnumConverter<E>) LOWERCASE_INSENSITIVE;
    }

    /**
     * Retrieves an {@link EnumConverter} accepting arguments that will be treated as all uppercase.
     *
     * @param <E> the enum type subclass.
     * @return the retrieved {@link EnumConverter}.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumConverter<E> upperCaseInsensitive() {
        return UPPERCASE_INSENSITIVE == null
                ? (EnumConverter<E>) (UPPERCASE_INSENSITIVE = (EnumConverter<E>) (nameArgument, directory) -> directory.get(nameArgument.toUpperCase(Locale.ROOT)))
                : (EnumConverter<E>) UPPERCASE_INSENSITIVE;
    }

    /**
     * Retrieves an {@link EnumConverter} accepting arguments that must be cased exactly as any of the defined constants
     * in the enum subtype {@link E}.
     *
     * @param <E> the enum type subclass.
     * @return the retrieved {@link EnumConverter}.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumConverter<E> caseSensitive() {
        return CASE_SENSITIVE == null
                ? (EnumConverter<E>) (CASE_SENSITIVE = (EnumConverter<E>) (nameArgument, directory) -> directory.get(nameArgument))
                : (EnumConverter<E>) CASE_SENSITIVE;
    }
}
