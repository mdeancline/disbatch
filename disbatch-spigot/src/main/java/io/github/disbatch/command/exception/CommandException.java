package io.github.disbatch.command.exception;

/**
 * @apiNote Not to be confused with {@link org.bukkit.command.CommandException}.
 *
 * @since 1.0.0
 */
public class CommandException extends RuntimeException {
    public CommandException(final String message) {
        super(message);
    }

    public CommandException(final Exception cause) {
        super(cause);
    }
}
