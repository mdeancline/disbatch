package io.github.disbatch.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles command failures by notifying the {@link CommandSender} with appropriate messages.
 *
 * @since 1.1.0
 */
public final class CommandFailureNotifier implements CommandFailureHandler {
    private final Map<CommandFailure.Reason, CommandFailureMessage> messages = new HashMap<>();

    @Override
    public void handle(final CommandSender sender, final CommandFailure failure) {
        final CommandFailure.Reason reason = failure.getReason();
        final CommandFailureMessage message = messages.get(reason);

        if (message != null)
            sender.sendMessage(message.toString(reason));
    }

    /**
     * Associates the same failure message with all remaining possible failure reasons.
     *
     * @param message the failure message template
     * @return the updated {@code CommandFailureNotifier} instance
     */
    public CommandFailureNotifier with(@NotNull final String message) {
        for (final CommandFailure.Reason reason : CommandFailure.Reason.values())
            if (!messages.containsKey(reason))
                with(reason, givenReason -> message.replaceAll("%reason", givenReason.toString()));

        return this;
    }

    /**
     * Associates a specific failure message with a specific failure reason.
     *
     * @param reason  the specific failure reason
     * @param message the failure message
     * @return the updated {@code CommandFailureNotifier} instance
     */
    public CommandFailureNotifier with(@NotNull final CommandFailure.Reason reason, @NotNull final String message) {
        return with(reason, givenReason -> message);
    }

    /**
     * Associates a specific failure message with a specific failure reason using a
     * {@code CommandFailureMessage}.
     *
     * @param reason  the specific failure reason
     * @param message the failure message instance
     * @return the updated {@code CommandFailureNotifier} instance
     */
    public CommandFailureNotifier with(@NotNull final CommandFailure.Reason reason, @NotNull final CommandFailureMessage message) {
        messages.put(reason, message);
        return this;
    }
}
