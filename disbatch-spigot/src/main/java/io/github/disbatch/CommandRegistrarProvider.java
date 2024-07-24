package io.github.disbatch;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

/**
 * Creates and provides a {@link CommandRegistrar} suitable for the server's environment.
 */
public final class CommandRegistrarProvider {
    private final Server server;
    private final int nmsVersion;
    private CommandRegistrar registrar;

    /**
     * Constructs a new {@code CommandRegistrarProvider} with the default Bukkit server instance.
     */
    public CommandRegistrarProvider() {
        this(Bukkit.getServer());
    }

    /**
     * Constructs a new {@code CommandRegistrarProvider} with the given server.
     *
     * @param server the Bukkit server instance
     */
    public CommandRegistrarProvider(@NotNull final Server server) {
        this.server = server;
        nmsVersion = getNmsVersion(server);
    }

    /**
     * Retrieves the {@link CommandRegistrar} suitable for the server's environment.
     * If <a href="https://github.com/Mojang/brigadier">Brigadier</a> is present, a corresponding registrar that can
     * link registered commands to its features is returned. Otherwise, a backward-compatible alternative is provided.
     *
     * @return the appropriate {@link CommandRegistrar} instance
     */
    public CommandRegistrar getRegistrar() {
        if (registrar == null)
            registrar = getVersionedRegistrar();

        return registrar;
    }

    private CommandRegistrar getVersionedRegistrar() {
        switch (nmsVersion) {
            case 1131:
                return new BrigadierCommandRegistrar_1_13_R1(server);
            case 1132:
                return new BrigadierCommandRegistrar_1_13_R2(server);
            case 1141:
                return new BrigadierCommandRegistrar_1_14_R1(server);
            case 1151:
                return new BrigadierCommandRegistrar_1_15_R1(server);
            case 1161:
                return new BrigadierCommandRegistrar_1_16_R1(server);
            case 1162:
                return new BrigadierCommandRegistrar_1_16_R2(server);
            case 1163:
                return new BrigadierCommandRegistrar_1_16_R3(server);
            case 1171:
                return new BrigadierCommandRegistrar_1_17_R1(server);
            case 1181:
                return new BrigadierCommandRegistrar_1_18_R1(server);
            case 1182:
                return new BrigadierCommandRegistrar_1_18_R2(server);
            case 1191:
                return new BrigadierCommandRegistrar_1_19_R1(server);
            case 1192:
                return new BrigadierCommandRegistrar_1_19_R2(server);
            case 1193:
                return new BrigadierCommandRegistrar_1_19_R3(server);
            case 1201:
                return new BrigadierCommandRegistrar_1_20_R1(server);
            case 1202:
                return new BrigadierCommandRegistrar_1_20_R2(server);
            case 1203:
                return new BrigadierCommandRegistrar_1_20_R3(server);
            default:
                return new BukkitCommandRegistrar(server);
        }
    }

    private int getNmsVersion(final Server server) {
        final String version = server.getVersion();
        final String nmsVersion = version.substring(version.lastIndexOf(".v"));
        final String numericNmsVersion = nmsVersion.replaceAll("_", "")
                .replaceAll("R", "");
        return Integer.parseInt(numericNmsVersion);
    }
}
