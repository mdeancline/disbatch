package io.github.disbatch;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandRegistration;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A namespace for retrieving the correct {@link CommandRegistrar} for registering {@link Command}s.
 *
 * @since 1.1.0
 */
public final class CommandRegistrars {
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?( .*)?\\)");
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
    public static CommandRegistrar getCompatibleRegistrar(@NotNull final JavaPlugin plugin) {
        return getCompatibleRegistrar(new PluginKey(plugin));
    }

    /**
     * Retrieves the {@link CommandRegistrar} most suitable for the given {@link CommandRegistrar.Key} and the server's
     * environment.
     *
     * @apiNote If <a href="https://github.com/Mojang/brigadier">Brigadier</a> is present on the server, an appropriate
     * {@code CommandRegistrar} that can link commandss to its features will be returned.
     */
    public static CommandRegistrar getCompatibleRegistrar(@NotNull final CommandRegistrar.Key key) {
        if (!REGISTRARS.containsKey(key)) {
            final Server server = key.getServer();
            final CommandRegistrar registrar = isBrigadierPresent()
                    ? getBrigadierRegistrar(server)
                    : getBukkitRegistrar(server);
            REGISTRARS.put(key, registrar);

            return registrar;
        }

        return REGISTRARS.get(key);
    }

    private static CommandRegistrar getBukkitRegistrar(final Server server) {
        final CommandRegistrar registrar = new BukkitCommandRegistrar(server);
        return server.getPluginManager().getPlugin("ProtocolLib") == null
                ? registrar
                : new DynamicUpdateRegistrar(registrar);
    }

    private static CommandRegistrar getBrigadierRegistrar(final Server server) {
        switch (getMinecraftVersion(server)) {
            case 113:
                return new BrigadierCommandRegistrar_1_13(server);
            case 117:
                return new BrigadierCommandRegistrar_1_17(server);
            default:
                return getBukkitRegistrar(server);
        }
    }

    private static int getMinecraftVersion(final Server server) {
        final Matcher matcher = VERSION_PATTERN.matcher(server.getVersion());

        if (matcher.find())
            return Integer.parseInt(matcher.toMatchResult().group(2), 10);
        else
            throw new CommandRegistrationException("Unable to extract the correct Minecraft version");
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

    private static class PluginKey extends CommandRegistrar.Key {
        private final JavaPlugin plugin;

        private PluginKey(final JavaPlugin plugin) {
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

    private static class DynamicUpdateRegistrar implements CommandRegistrar {
        private final CommandRegistrar source;

        private DynamicUpdateRegistrar(final CommandRegistrar source) {
            this.source = source;
        }

        @Override
        public void register(@NotNull final CommandRegistration registration) {
            source.register(registration);
            updateClientCommandLists();
        }

        @Override
        public void registerFromFile(@NotNull final CommandRegistration registration) {
            source.registerFromFile(registration);
            updateClientCommandLists();
        }

        private void updateClientCommandLists() {

        }
    }
}
