package io.github.disbatch;

import com.google.common.reflect.TypeToken;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The primary namespace dedicated to registering a {@link Command}.
 *
 * @deprecated use {@link CommandRegistrar} instead
 *
 * @since 1.0.0
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
     * @param label the label that should be used to execute the {@code Command}.
     * @see Disbatch#register(Command, CommandDescriptor)
     *
     * @deprecated use {@link CommandRegistrar#register(CommandDescriptor)} instead
     */
    public static <S extends CommandSender> void register(final @NotNull Command<S> command, final @NotNull String label) {
        final Class<S> senderType = (Class<S>) new TypeToken<S>(command.getClass()){}.getRawType();
        register(command, CommandDescriptor.of(senderType, command).build());
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     * @see Disbatch#register(Command, String)
     *
     * @deprecated use {@link CommandRegistrar#register(CommandDescriptor)} instead
     */
    public static <S extends CommandSender> void register(final @NotNull Command<S> command, final @NotNull CommandDescriptor<?, ?> descriptor) {
        final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(command.getClass());
        final Class<S> senderType = (Class<S>) new TypeToken<S>(command.getClass()){}.getRawType();
        final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(plugin);
        registrar.register(descriptor);
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param label the label that should be used to execute the {@code Command}.
     * @param plugin the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, CommandDescriptor, JavaPlugin)
     *
     * @deprecated use {@link CommandRegistrar#registerFromFile(CommandDescriptor)} instead
     */
    public static <S extends CommandSender> void register(final @NotNull Command<S> command, final @NotNull String label, final @NotNull JavaPlugin plugin) {
        register(command, CommandDescriptor.of((Class<S>) new TypeToken<S>(command.getClass()) {}.getRawType(), command)
                .label(label)
                .build(), plugin);
    }

    //TODO create temporary mechanism
    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     * @param plugin the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, String, JavaPlugin)
     *
     * @deprecated use {@link CommandRegistrar#registerFromFile(CommandDescriptor)} instead
     */
    public static <S extends CommandSender> void register(final @NotNull Command<S> command, final @NotNull CommandDescriptor<?, ?> descriptor, final @NotNull JavaPlugin plugin) {
    }
}
