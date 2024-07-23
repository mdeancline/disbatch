package io.github.disbatch;

import com.google.common.reflect.TypeToken;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandRegistration;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The primary namespace dedicated to registering a {@link Command}.
 *
 * @since 1.0.0
 * @deprecated use {@link CommandRegistrar} instead
 */
@SuppressWarnings({"unchecked", "UnstableApiUsage"})
@Deprecated
public final class Disbatch {
    private Disbatch() {
        throw new AssertionError();
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server.
     *
     * @param command the {@code Command} to be registered.
     * @param label   the label that should be used to execute the {@code Command}.
     * @see Disbatch#register(Command, CommandRegistration)
     * @deprecated use {@link CommandRegistrar#register(CommandRegistration)} instead
     */
    public static <S extends CommandSender> void register(@NotNull final Command<S> command, @NotNull final String label) {
        final Class<S> senderType = (Class<S>) new TypeToken<S>(command.getClass()) {
        }.getRawType();
        register(command, CommandRegistration.builder(senderType, command).build());
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server.
     *
     * @param command      the {@code Command} to be registered.
     * @param registration the {@link CommandRegistration} aiding in providing usage help in the server's {@code /help} menu.
     * @see Disbatch#register(Command, String)
     * @deprecated use {@link CommandRegistrar#register(CommandRegistration)} instead
     */
    public static <S extends CommandSender> void register(@NotNull final Command<S> command, @NotNull final CommandRegistration registration) {
        if (registration.getLabel() == null)
            throw new CommandRegistrationException("Command label cannot be empty");

        final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(command.getClass());
        final Class<S> senderType = (Class<S>) new TypeToken<S>(command.getClass()) {
        }.getRawType();
        final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(plugin);
        registrar.register(registration);
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param label   the label that should be used to execute the {@code Command}.
     * @param plugin  the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, CommandRegistration, JavaPlugin)
     * @deprecated use {@link CommandRegistrar#registerFromFile(CommandRegistration)} instead
     */
    public static <S extends CommandSender> void register(@NotNull final Command<S> command, @NotNull final String label, @NotNull final JavaPlugin plugin) {
        register(command, CommandRegistration.builder((Class<S>) new TypeToken<S>(command.getClass()) {
                }.getRawType(), command)
                .label(label)
                .build(), plugin);
    }

    //TODO create temporary mechanism

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command      the {@code Command} to be registered.
     * @param registration the {@link CommandRegistration} aiding in providing usage help in the server's {@code /help} menu.
     * @param plugin       the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, String, JavaPlugin)
     * @deprecated use {@link CommandRegistrar#registerFromFile(CommandRegistration)} instead
     */
    public static <S extends CommandSender> void register(@NotNull final Command<S> command, @NotNull final CommandRegistration registration, @NotNull final JavaPlugin plugin) {
    }
}
