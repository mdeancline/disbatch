package io.github.disbatch;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.Nullable;

/**
 * Creates and provides a {@link CommandRegistrar} suitable for the server's environment.
 */
public final class CommandRegistrarProvider {
    private static final int CLIENT_COMMANDS_UPDATE_INTRO = 1131;

    private final int versionTarget;
    private CommandRegistrar registrar;

    /**
     * Constructs a new {@code CommandRegistrarProvider}.
     */
    public CommandRegistrarProvider() {
        // OBC and NMS package relocation was removed in PaperMC as of 1.20.5
        final int nmsVersion = getNmsVersion(Bukkit.getServer());
        versionTarget = nmsVersion == -1 ? getMinecraftVersion(Bukkit.getServer()) : nmsVersion;
    }

    /**
     * Retrieves the appropriate {@link CommandRegistrar} based on the server's environment.
     * <p>
     * If the server has loaded the <a href="https://github.com/Mojang/brigadier">Brigadier</a> library, this method
     * will return a registrar that integrates with Brigadier to link registered {@link Command}s to its features. If
     * Brigadier is not present, a backward-compatible registrar will be provided as an alternative. For more details on
     * these features, see this
     * <a href="https://www.minecraft.net/fi-fi/article/programmers-play-minecrafts-inner-workings">article</a>.
     * </p>
     * <p>
     * Additionally, if the server is running Minecraft version 1.13 or later, the returned registrar will also be capable
     * of sending command update packets to all online players.
     * </p>
     *
     * @return the appropriate {@link CommandRegistrar} instance based on the server environment and version.
     */
    public CommandRegistrar getRegistrar() {
        if (registrar == null) {
            final CommandDispatcherContext context = getContext();
            final CommandRegistrar registrar = context == null
                    ? new BukkitCommandRegistrar()
                    : new BrigadierCommandRegistrar(context);

            this.registrar = versionTarget >= CLIENT_COMMANDS_UPDATE_INTRO
                    ? new ClientCommandsUpdateRegistrar(registrar)
                    : registrar;
        }

        return registrar;
    }

    @Nullable
    private CommandDispatcherContext getContext() {
        switch (versionTarget) {
            case 1131:
                return new CommandDispatcherContext_1_13_R1();
            case 1132:
                return new CommandDispatcherContext_1_13_R2();
            case 1141:
                return new CommandDispatcherContext_1_14_R1();
            case 1151:
                return new CommandDispatcherContext_1_15_R1();
            case 1161:
                return new CommandDispatcherContext_1_16_R1();
            case 1162:
                return new CommandDispatcherContext_1_16_R2();
            case 1163:
                return new CommandDispatcherContext_1_16_R3();
            case 1171:
                return new CommandDispatcherContext_1_17_R1();
            case 1181:
                return new CommandDispatcherContext_1_18_R1();
            case 1182:
                return new CommandDispatcherContext_1_18_R2();
            case 1191:
                return new CommandDispatcherContext_1_19_R1();
            case 1192:
                return new CommandDispatcherContext_1_19_R2();
            case 1193:
                return new CommandDispatcherContext_1_19_R3();
            case 1201:
                return new CommandDispatcherContext_1_20_R1();
            case 1202:
                return new CommandDispatcherContext_1_20_R2();
            case 1203:
                return new CommandDispatcherContext_1_20_R3();
            default:
                return null;
        }
    }

    private int getNmsVersion(final Server server) {
        final String packageName = server.getClass().getPackage().getName();
        final int substringStartIndex = packageName.lastIndexOf(".v");
        if (substringStartIndex == -1) return -1;

        final String nmsVersion = packageName.substring(substringStartIndex);
        final String numericNmsVersion = nmsVersion.replaceAll("_", "")
                .replaceAll("R", "");
        return Integer.parseInt(numericNmsVersion);
    }

    private int getMinecraftVersion(final Server server) {
        final String version = server.getBukkitVersion();
        final String minecraftVersion = version.substring(0, version.indexOf("-"));
        final String numericMinecraftVersion = minecraftVersion.replaceAll("\\.", "");
        return Integer.parseInt(numericMinecraftVersion);
    }
}
