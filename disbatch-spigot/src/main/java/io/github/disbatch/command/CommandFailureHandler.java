package io.github.disbatch.command;

import org.bukkit.command.CommandSender;

/**
 * Handles the external failure of a command execution by processing failure details and providing feedback.
 * This enables custom handling of command failures, including displaying feedback and addressing specific
 * failure scenarios.
 *
 * @since 1.1.0
 */
public interface CommandFailureHandler {

    /**
     * Processes the external failure of a command execution.
     *
     * @param sender  the sender of the command who encountered the failure
     * @param failure details of the failure, including the reason and the associated command input
     */
    void handle(CommandSender sender, CommandFailure failure);
}
