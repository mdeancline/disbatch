package io.github.disbatch.command;

import org.bukkit.command.CommandSender;

//TODO complete documentation
/**
 * @since 1.1.0
 */
public interface CommandFailureHandler {
    void handle(CommandSender sender, CommandFailure failure);
}
