package io.github.disbatch;

import io.github.disbatch.command.Command;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A namespace for retrieving the correct {@link CommandRegistrar} for registering {@link Command}s.
 *
 * @since 1.1.0
 */
public final class CommandRegistrars {
    private static final String BRIGADIER_PACKAGE_NAME = "com.mojang.brigadier";
    private static final Map<CommandRegistrar.Key, CommandRegistrar> REGISTRARS = new HashMap<>();

    private CommandRegistrars() {
        throw new AssertionError();
    }

    /**
     * Retrieves the {@link CommandRegistrar} most suitable for the given {@link JavaPlugin}'s server environment.
     *
     * @apiNote If <a href="https://github.com/Mojang/brigadier">Brigadier</a> is present on the server, an appropriate
     * {@code CommandRegistrar} that can link commands to its features will be returned.
     */
    public static CommandRegistrar getCompatibleRegistrar(final @NotNull JavaPlugin plugin) {
        return getCompatibleRegistrar(new PluginKey(plugin));
    }

    /**
     * Retrieves the {@link CommandRegistrar} most suitable for the given {@link CommandRegistrar.Key} and the server's
     * environment.
     *
     * @apiNote If <a href="https://github.com/Mojang/brigadier">Brigadier</a> is present on the server, an appropriate
     * {@code CommandRegistrar} that can link commandss to its features will be returned.
     */
    public static CommandRegistrar getCompatibleRegistrar(final @NotNull CommandRegistrar.Key key) {
        if (!REGISTRARS.containsKey(key)) {
            final Server server = key.getServer();
            final CommandRegistrar registrar = isBrigadierPresent()
                    ? new BrigadierCommandRegistrar(server)
                    : new BukkitCommandRegistrar(server);

            REGISTRARS.put(key, server.getPluginManager().getPlugin("ProtocolLib") == null
                    ? registrar
                    : new DynamicUpdateRegistrar(registrar));

            return registrar;
        }

        return REGISTRARS.get(key);
    }

    //TODO develop different system of verifying Brigadier presence
    private static boolean isBrigadierPresent() {
        final String javaVersion = System.getProperty("java.version");
//        final Package brigadierPackage = javaVersion.startsWith("9.")
//                ? ClassLoader.getSystemClassLoader().getDefinedPackage(BRIGADIER_PACKAGE_NAME)
//                : Package.getPackage(BRIGADIER_PACKAGE_NAME);
        final Package brigadierPackage = Package.getPackage(BRIGADIER_PACKAGE_NAME);

        return brigadierPackage != null;
    }

    public static class PluginKey extends CommandRegistrar.Key {
        private final JavaPlugin plugin;

        public PluginKey(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public int hashCode() {
            return plugin.getName().hashCode();
        }

        @Override
        public Server getServer() {
            return plugin.getServer();
        }
    }
}
