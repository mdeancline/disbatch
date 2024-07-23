package io.github.disbatch;

import io.github.disbatch.command.CommandRegistration;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for the registration of commands.
 *
 * @see CommandRegistrars#getCompatibleRegistrar(JavaPlugin)
 * @see CommandRegistrars#getCompatibleRegistrar(Key)
 * @since 1.1.0
 */
public interface CommandRegistrar {

    /**
     * Registers a command to be used on the Spigot Minecraft server without needing to be defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param registration the registration for registering a command to the server.
     * @throws CommandRegistrationException if there is an error during the command registration process.
     * @see CommandRegistrar#registerFromFile(CommandRegistration)
     */
    void register(@NotNull CommandRegistration registration);

    /**
     * Registers a command to be used on the Spigot Minecraft server as defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param registration the registration for registering a command to the server.
     * @throws CommandRegistrationException if there is an error during the command registration process.
     * @see CommandRegistrar#register(CommandRegistration)
     */
    void registerFromFile(@NotNull CommandRegistration registration);

    /**
     * Represents a key for identifying and retrieving a {@link CommandRegistrar}.
     */
    abstract class Key {

        /**
         * Returns a hash code value for the key unique to the key's properties.
         *
         * @return a hash code value.
         */
        @Override
        public abstract int hashCode();

        /**
         * Gets the server associated with this key.
         *
         * @return the server.
         */
        public abstract Server getServer();
    }
}
