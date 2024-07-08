package io.github.disbatch;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.CommandExecutionException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The primary namespace dedicated to registering a {@link Command}.
 *
 * @since 1.0.0
 */
public final class Disbatch {
    private static final CommandRegistrar REGISTRAR = new LegacyCommandRegistrar(Bukkit.getServer());

    private Disbatch() {
        throw new AssertionError();
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server.
     *
     * @param command the {@code Command} to be registered.
     * @param label the label that should be used to execute the {@code Command}.
     * @see Disbatch#register(Command, CommandDescriptor)
     */
    public static void register(final @NotNull Command<?> command, final @NotNull String label) {
        register(command, new CommandDescriptor.Builder().label(label).build());
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     * @see Disbatch#register(Command, String)
     */
    public static void register(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor) {
        REGISTRAR.register(new TypedCommandProxy(command, descriptor.getValidSenderMessage()), descriptor);
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param label the label that should be used to execute the {@code Command}.
     * @param plugin the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, CommandDescriptor, JavaPlugin)
     */
    public static void register(final @NotNull Command<?> command, final @NotNull String label, final @NotNull JavaPlugin plugin) {
        register(command, new CommandDescriptor.Builder().label(label).build(), plugin);
    }

    /**
     * Registers a {@link Command} to be used on the Spigot Minecraft server pertaining to a specific {@link JavaPlugin}.
     *
     * @param command the {@code Command} to be registered.
     * @param descriptor the {@link CommandDescriptor} aiding in providing usage help in the server's {@code /help} menu.
     * @param plugin the plugin chosen to have the given {@code Command} registered.
     * @see Disbatch#register(Command, String, JavaPlugin)
     */
    public static void register(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor, final @NotNull JavaPlugin plugin) {
        setupPluginCommandExecution(command, descriptor, plugin);
        plugin.getServer().getHelpMap().addTopic(new CommandTopicAdapter(descriptor.getLabel(), descriptor.getTopic()));
    }

    private static void setupPluginCommandExecution(final Command<?> command, final CommandDescriptor descriptor, final JavaPlugin plugin) {
        final PluginCommand pluginCommand = getExistingPluginCommand(plugin, descriptor.getLabel());
        final TypedCommandProxy proxy = new TypedCommandProxy(command, descriptor.getValidSenderMessage());

        pluginCommand.setExecutor((sender, serverCommand, label, args) -> {
            if (sender == null) throw new CommandExecutionException("CommandSender is null");
            proxy.execute(sender, computeInput(label, args));
            return true;
        });

        pluginCommand.setTabCompleter((sender, serverCommand, label, args)
                -> proxy.tabComplete(sender, computeInput(label, args)));
    }

    private static PluginCommand getExistingPluginCommand(final JavaPlugin plugin, final String commandLabel) {
        final PluginCommand pluginCommand = plugin.getCommand(commandLabel);

        if (pluginCommand == null)
            throw new CommandRegistrationException(String.format("Command \"%s\" is not registered in the plugin.yml file of %s",
                    commandLabel, plugin.getName()));

        return pluginCommand;
    }

    private static CommandInput computeInput(final String label, final String[] args) {
        return args.length > 0 ? new LazyLoadingCommandInput(args, label) : new SingleLabelCommandInput(label);
    }
}
