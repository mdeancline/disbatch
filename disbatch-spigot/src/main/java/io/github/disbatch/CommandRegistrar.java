package io.github.disbatch;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for the registration of {@link Command}s.
 *
 * @see CommandRegistrars#getCompatibleRegistrar(JavaPlugin)
 * @since 1.1.0
 */
public interface CommandRegistrar {

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server without needing to be defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param label the label that should be used to execute the {@code Command}.
     *
     * @throws CommandRegistrationException
     *
     * @see CommandRegistrar#register(Command, CommandDescriptor)
     */
    void register(final @NotNull Command<?> command, final @NotNull String label);

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server without needing to be defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     *
     * @throws CommandRegistrationException
     *
     * @see CommandRegistrar#register(Command, String)
     */
    void register(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor);

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server as defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param label the label that should be used to execute the {@code Command}.
     *
     * @throws CommandRegistrationException
     *
     * @see CommandRegistrar#register(Command, CommandDescriptor)
     */
    void registerFromFile(final @NotNull Command<?> command, final @NotNull String label);

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server as defined in the
     * <a href="https://www.spigotmc.org/wiki/plugin-yml/">plugin.yml</a> file belonging to the
     * {@code CommandRegistrar}'s connected {@link Plugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     *
     * @throws CommandRegistrationException
     *
     * @see CommandRegistrar#register(Command, String)
     */
    void registerFromFile(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor);
}
