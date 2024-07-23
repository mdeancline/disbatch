package io.github.disbatch;

import io.github.disbatch.command.Command;
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
            final CommandRegistrar registrar = getCompatibleRegistrar(key.getServer());
            REGISTRARS.put(key, registrar);
            return registrar;
        }

        return REGISTRARS.get(key);
    }

    private static CommandRegistrar getCompatibleRegistrar(final Server server) {
        switch (getMinecraftServerVersion(server)) {
            case 1131:
                return new BrigadierCommandRegistrar_1_13_R1(server);
            case 1132:
                return new BrigadierCommandRegistrar_1_13_R2(server);
            case 1141:
                return new BrigadierCommandRegistrar_1_14_R1(server);
            case 1171:
                return new BrigadierCommandRegistrar_1_17_R1(server);
            default:
                return new BukkitCommandRegistrar(server);
        }
    }

    private static int getMinecraftServerVersion(final Server server) {
        final Matcher matcher = VERSION_PATTERN.matcher(server.getVersion());
        return matcher.find() ? Integer.parseInt(matcher.toMatchResult().group(2), 10) : -1;
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
}
