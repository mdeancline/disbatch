package io.github.disbatch.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A namespace for {@link TabCompleter} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class TabCompleters {
    private static final TabCompleter<?> EMPTY = (sender, input) -> ImmutableList.of();

    private TabCompleters() {
        throw new AssertionError();
    }

    /**
     * @param <S>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S extends CommandSender> TabCompleter<S> empty() {
        return (TabCompleter<S>) EMPTY;
    }

    /**
     * @param values
     * @param <S>
     * @param <E>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender, E extends Enum<E>> TabCompleter<S> of(final @NotNull E[] values) {
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
    public static <S extends CommandSender, E extends Enum<E>> TabCompleter<S> ofLowerCase(final @NotNull E[] values) {
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
    public static <S extends CommandSender> TabCompleter<S> of(final @NotNull String... values) {
        return of(Lists.newArrayList(values));
    }

    /**
     * @param collection
     * @param <S>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> TabCompleter<S> of(final @NotNull Collection<String> collection) {
        return (sender, input) -> collection;
    }

    /**
     *
     * @param values
     * @return
     * @param <S>
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> TabCompleter<S> forFirstArgument(final @NotNull String... values) {
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
    public static <S extends CommandSender> TabCompleter<S> forFirstArgument(final @NotNull Collection<String> collection) {
        return forFirstArgument(of(collection));
    }

    /**
     * @param completer
     * @param <S>
     * @return
     *
     * @since 1.1.0
     */
    public static <S extends CommandSender> TabCompleter<S> forFirstArgument(final @NotNull TabCompleter<S> completer) {
        return (sender, input) -> {
            final int length = input.getArgumentLength();
            return length == 1
                    ? completer.tabComplete(sender, input)
                    : ImmutableList.of();
        };
    }
}
