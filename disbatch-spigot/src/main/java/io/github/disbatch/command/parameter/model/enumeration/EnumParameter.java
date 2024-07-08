package io.github.disbatch.command.parameter.model.enumeration;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.parameter.AbstractParameter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses an enum based on a parsable, passed argument.
 *
 * @param <S> {@inheritDoc}
 * @param <E> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class EnumParameter<S extends CommandSender, E extends Enum<E>> extends AbstractParameter<S, E> {
    private static final Map<Class<Enum<?>>, EnumRepository<?>> ENUM_REPOSITORY_CACHE = new HashMap<>();

    private final EnumRepository<E> repository;
    private final EnumConverter<E> converter;

    /**
     * @param type
     */
    public EnumParameter(final @NotNull Class<E> type) {
        this(type, EnumConverters.caseSensitive());
    }

    /**
     * @param type
     * @param converter
     */
    @SuppressWarnings("unchecked")
    public EnumParameter(final @NotNull Class<E> type, final @NotNull EnumConverter<E> converter) {
        this.converter = converter;

        repository = (EnumRepository<E>) ENUM_REPOSITORY_CACHE.computeIfAbsent((Class<Enum<?>>) type,
                enumClass -> new MapBasedEnumRepository(type));
    }

    @Override
    public @Nullable E parse(final S sender, final CommandInput input) {
        return converter.convertWith(input.getArgument(0), repository);
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }

    private class MapBasedEnumRepository implements EnumRepository<E> {
        private final Map<String, E> enumConstants = new HashMap<>();

        private MapBasedEnumRepository(final Class<E> type) {
            for (final E enumConstant : type.getEnumConstants())
                enumConstants.put(enumConstant.name(), enumConstant);
        }

        @Override
        public @Nullable E get(final String nameArgument) {
            return enumConstants.get(nameArgument);
        }
    }
}