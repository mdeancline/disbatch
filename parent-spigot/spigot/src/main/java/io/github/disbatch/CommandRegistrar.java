package io.github.disbatch;

import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for the registration of commands.
 *
 * @see CommandRegistrarProvider#getRegistrar()
 * @since 1.1.0
 */
public interface CommandRegistrar {

    /**
     * Registers a command to be used on the Spigot Minecraft server without needing to be defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the registrar's connected
     * {@link Plugin}.
     *
     * @param command the registration for registering a command to the server.
     * @throws CommandRegistrationException if there is an error during the command registration process.
     */
    void register(@NotNull Command command);
}
