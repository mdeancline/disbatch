package io.github.disbatch;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandDescriptor;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for the registration of commands.
 *
 * @see CommandRegistrars#getCompatibleRegistrar(JavaPlugin)
 *
 * @since 1.1.0
 */
public interface CommandRegistrar {

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server without needing to be defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param label the label that should be used for this builder
     * @param descriptor the descriptor for registering a command to the server.
     * @throws CommandRegistrationException
     * @see CommandRegistrar#registerFromFile(String, CommandDescriptor)
     */
    void register(@NotNull String label, @NotNull CommandDescriptor descriptor);

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server as defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param label the label that should be used for this builder
     * @param descriptor the descriptor for registering a command to the server.
     * @throws CommandRegistrationException
     * @see CommandRegistrar#register(String, CommandDescriptor)
     */
    void registerFromFile(@NotNull String label, @NotNull CommandDescriptor descriptor);

    //TODO complete documentation
    abstract class Key {
        @Override
        public abstract int hashCode();
        public abstract Server getServer();
    }
}
