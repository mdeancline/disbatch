package io.github.disbatch.command.syntax.model.enumeration;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.Suggesters;
import io.github.disbatch.command.syntax.AbstractSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses an {@code Enum} type based on a parsable, passed argument.
 * <p>
 * <b>Input syntax:</b> [enum type]
 *
 * @param <S> {@inheritDoc}
 * @param <E> {@inheritDoc}
 * @see Suggesters#of(Enum[])
 * @since 1.1.0
 */
public final class EnumSyntax<S extends CommandSender, E extends Enum<E>> extends AbstractSyntax<S, E> {
    private static final Map<Class<Enum<?>>, EnumRepository<?>> ENUM_REPOSITORY_CACHE = new HashMap<>();

    private final EnumRepository<E> repository;
    private final EnumConverter<E> converter;

    /**
     * Constructs a new {@code EnumSyntax} with the specified argument label and enum type.
     */
    public EnumSyntax(@NotNull final String label, @NotNull final Class<E> type) {
        this(label, type, EnumConverters.caseSensitive());
    }

    /**
     * Constructs a new {@code EnumSyntax} with the specified argument label, enum type, and converter.
     */
    @SuppressWarnings("unchecked")
    public EnumSyntax(@NotNull final String label, @NotNull final Class<E> type, @NotNull final EnumConverter<E> converter) {
        super(label);
        this.converter = converter;

        repository = (EnumRepository<E>) ENUM_REPOSITORY_CACHE.computeIfAbsent((Class<Enum<?>>) type,
                enumClass -> new MapBasedEnumRepository(type));
    }

    @Override
    public @Nullable E parse(final S sender, final CommandInput input) {
        return converter.convertWith(input.getArgument(0), repository);
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return false;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }

    @Override
    protected boolean isGreedy() {
        return false;
    }

    private class MapBasedEnumRepository implements EnumRepository<E> {
        private final Map<String, E> enumConstants = new HashMap<>();

        private MapBasedEnumRepository(final Class<E> type) {
            for (final E enumConstant : type.getEnumConstants())
                enumConstants.put(enumConstant.name(), enumConstant);
        }

        @Override
        public @Nullable E get(final String name) {
            return enumConstants.get(name);
        }
    }
}