package io.github.disbatch.command;

import net.md_5.bungee.api.CommandSender;

/**
 * Represents the BungeeCord proxy console.
 *
 * @apiNote The BungeeCord API has no way of exposing the console on its own. Therefore, the BungeeCord version
 * of Disbatch has its own implementations to work with the console.
 *
 * @since 1.0.0
 */
public interface ConsoleCommandSender extends CommandSender {
}
