package io.github.disbatch.command.builder;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * A namespace for {@link TabCompleter} convenience and utility methods.
 */
@ApiStatus.AvailableSince("1.0")
public final class TabCompleters {
    private static final TabCompleter<?> EMPTY = new EmptyTabCompleter();
    private TabCompleters() {
        throw new AssertionError();
    }

    /**
     * Retrieves an empty {@link TabCompleter}.
     *
     * @param <S>
     * @return the empty {@code TabCompleter}.
     */
    @SuppressWarnings("unchecked")
    public static <S extends CommandSender> TabCompleter<S> empty() {
        return (TabCompleter<S>) EMPTY;
    }

    private static class EmptyTabCompleter implements TabCompleter<CommandSender> {
        @Override
        public List<String> tabComplete(final CommandSender sender, final CommandInput input) {
            return ImmutableList.of();
        }
    }
}
