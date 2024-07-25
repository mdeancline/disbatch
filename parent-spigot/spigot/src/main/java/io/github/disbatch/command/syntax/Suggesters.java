package io.github.disbatch.command.syntax;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A namespace for {@link Suggester} convenience and utility methods.
 *
 * @since 1.0.0
 */
@SuppressWarnings("ConstantConditions")
public final class Suggesters {
    private static final Suggestion EMPTY_SUGGESTION = Suggestion.of(StringUtils.EMPTY);
    private static final Suggester<?> EMPTY = (sender, input) -> Collections.singleton(EMPTY_SUGGESTION);

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
     * @since 1.1.0
     */
    public static <S extends CommandSender, E extends Enum<E>> Suggester<S> of(@NotNull final E[] values) {
        return of(Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    /**
     * @param values
     * @param <S>
     * @param <E>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender, E extends Enum<E>> Suggester<S> ofLowerCase(@NotNull final E[] values) {
        return of(Arrays.stream(values)
                .map(e -> e.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList()));
    }

    /**
     * @param values
     * @param <S>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> of(@NotNull final Collection<String> values) {
        return of((String[]) values.toArray());
    }

    /**
     * @param values
     * @param <S>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> of(@NotNull final String... values) {
        final Collection<Suggestion> suggestions = Suggestion.ofTexts(values);
        return (sender, input) -> suggestions;
    }

    /**
     * @param values
     * @param <S>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(@NotNull final String... values) {
        return forFirstArgument(Lists.newArrayList(values));
    }

    /**
     * @param collection
     * @param <S>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(@NotNull final Collection<String> collection) {
        return forFirstArgument(of(collection));
    }

    /**
     * @param completer
     * @param <S>
     * @return
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(@NotNull final Suggester<S> completer) {
        return (sender, input) -> {
            final int length = input.length;
            return length == 1
                    ? completer.getSuggestions(sender, input)
                    : Collections.emptyList();
        };
    }
}
