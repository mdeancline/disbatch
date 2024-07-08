package io.github.disbatch.command.parameter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.disbatch.command.TabCompleters;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A namespace for {@link Suggester} convenience and utility methods.
 *
 * @since 1.0.0
 *
 * @deprecated planned to be replaced with {@link TabCompleters}
 */
@Deprecated
public final class Suggesters {
    private static final Suggester<?> EMPTY = (sender, input) -> ImmutableList.of();

    private Suggesters() {
        throw new AssertionError();
    }

    /**
     * @param <S>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S extends CommandSender> Suggester<S> empty() {
        return (Suggester<S>) EMPTY;
    }

    /**
     * @param values
     * @param <S>
     * @param <E>
     * @return
     */
    public static <S extends CommandSender, E extends Enum<E>> Suggester<S> of(final @NotNull E[] values) {
        return of(Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    /**
     * @param values
     * @param <S>
     * @param <E>
     * @return
     */
    public static <S extends CommandSender, E extends Enum<E>> Suggester<S> ofLowerCase(final @NotNull E[] values) {
        return of(Arrays.stream(values)
                .map(e -> e.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList()));
    }

    /**
     * @param elements
     * @param <S>
     * @return
     */
    public static <S extends CommandSender> Suggester<S> of(final @NotNull String... elements) {
        return of(Lists.newArrayList(elements));
    }

    /**
     * @param collection
     * @param <S>
     * @return
     */
    public static <S extends CommandSender> Suggester<S> of(final @NotNull Collection<String> collection) {
        return (sender, input) -> collection;
    }

    /**
     * @param suggester
     * @param <S>
     * @return
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(final @NotNull Suggester<S> suggester) {
        return (sender, input) -> {
            final int length = input.getArgumentLength();
            return length == 1
                    ? suggester.getSuggestions(sender, input)
                    : ImmutableList.of();
        };
    }
}
