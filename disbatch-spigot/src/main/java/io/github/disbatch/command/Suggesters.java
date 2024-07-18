package io.github.disbatch.command;

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
     *
     * @since 1.1.0
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
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender, E extends Enum<E>> Suggester<S> ofLowerCase(final @NotNull E[] values) {
        return of(Arrays.stream(values)
                .map(e -> e.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList()));
    }

    /**
     * @param values
     * @param <S>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> of(final @NotNull Collection<String> values) {
        return of((String[]) values.toArray());
    }

    /**
     * @param values
     * @param <S>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> of(final @NotNull String... values) {
        final Collection<Suggestion> suggestions = Suggestion.ofTexts(values);
        return (sender, input) -> suggestions;
    }

    /**
     *
     * @param values
     * @return
     * @param <S>
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(final @NotNull String... values) {
        return forFirstArgument(Lists.newArrayList(values));
    }

    /**
     *
     * @param collection
     * @return
     * @param <S>
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(final @NotNull Collection<String> collection) {
        return forFirstArgument(of(collection));
    }

    /**
     * @param completer
     * @param <S>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> Suggester<S> forFirstArgument(final @NotNull Suggester<S> completer) {
        return (sender, input) -> {
            final int length = input.length;
            return length == 1
                    ? completer.getSuggestions(sender, input)
                    : Collections.emptyList();
        };
    }
}
