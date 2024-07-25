package io.github.disbatch;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

/**
 * Creates and provides a {@link CommandRegistrar} suitable for the server's environment.
 */
public final class CommandRegistrarProvider {
    private static final int UPDATE_COMMANDS_MIN_VERSION = 1_13_1;

    private final int versionTarget;
    private CommandRegistrar registrar;

    /**
     * Constructs a new {@code CommandRegistrarProvider}.
     */
    public CommandRegistrarProvider() {
        // OBC and NMS package relocation was removed in PaperMC as of 1.20.5
        final int revisionVersion = getRevisionVersion();
        versionTarget = revisionVersion == -1 ? getMajorMinorVersion() : revisionVersion;
    }

    /**
     * Retrieves the appropriate {@link CommandRegistrar} based on the server's environment.
     * <p>
     * If the server has loaded the <a href="https://github.com/Mojang/brigadier">Brigadier</a> library, this method
     * will return a registrar that integrates with Brigadier, enabling registered {@link Command}s to utilize its features.
     * If Brigadier is not present, a backward-compatible registrar will be provided as an alternative. For more details on
     * Minecraft's Brigadier-powered command features, see this
     * <a href="https://www.minecraft.net/fi-fi/article/programmers-play-minecrafts-inner-workings">article</a>.
     * </p>
     * <p>
     * Additionally, if the server is running Minecraft version 1.13 or later, the returned registrar will also be capable
     * of sending command update packets to all online players whenever a new command is registered.
     * </p>
     *
     * @return the appropriate {@link CommandRegistrar} instance based on the server environment and version.
     */
    public CommandRegistrar getRegistrar() {
        if (registrar == null) {
            final BrigadierRegistryContext context = getContext();
            final CommandRegistrar registrar = context == null
                    ? new BukkitCommandRegistrar()
                    : new BrigadierCommandRegistrar(context);

            this.registrar = versionTarget >= UPDATE_COMMANDS_MIN_VERSION
                    ? new ClientCommandsUpdateRegistrar(registrar)
                    : registrar;
        }

        return registrar;
    }

    @Nullable
    private BrigadierRegistryContext getContext() {
        switch (versionTarget) {
            case 1_20:
            case 1_21:
                return new PrimaryBrigadierRegistryContext();
            case 1_13_1:
                return new BrigadierRegistryContext_1_13_R1();
            case 1_13_2:
                return new BrigadierRegistryContext_1_13_R2();
            case 1_14_1:
                return new BrigadierRegistryContext_1_14_R1();
            case 1_15_1:
                return new BrigadierRegistryContext_1_15_R1();
            case 1_16_1:
                return new BrigadierRegistryContext_1_16_R1();
            case 1_16_2:
                return new BrigadierRegistryContext_1_16_R2();
            case 1_16_3:
                return new BrigadierRegistryContext_1_16_R3();
            case 1_17_1:
                return new BrigadierRegistryContext_1_17_R1();
            case 1_18_1:
                return new BrigadierRegistryContext_1_18_R1();
            case 1_18_2:
                return new BrigadierRegistryContext_1_18_R2();
            case 1_19_1:
                return new BrigadierRegistryContext_1_19_R1();
            case 1_19_2:
                return new BrigadierRegistryContext_1_19_R2();
            case 1_19_3:
                return new BrigadierRegistryContext_1_19_R3();
            case 1_20_1:
                return new BrigadierRegistryContext_1_20_R1();
            case 1_20_2:
                return new BrigadierRegistryContext_1_20_R2();
            case 1_20_3:
                return new BrigadierRegistryContext_1_20_R3();
            case 1_20_4:
                return new BrigadierRegistryContext_1_20_R4();
            case 1_21_1:
                return new BrigadierRegistryContext_1_21_R1();
            default:
                return null;
        }
    }

    private int getRevisionVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        final int substringStartIndex = packageName.lastIndexOf(".v");
        if (substringStartIndex == -1) return -1;

        String nmsVersion = packageName.substring(substringStartIndex);
        nmsVersion = nmsVersion.replaceAll("_", "").replaceFirst("R", "");
        return Integer.parseInt(nmsVersion);
    }

    private int getMajorMinorVersion() {
        String version = Bukkit.getBukkitVersion();
        version = version.substring(0, version.indexOf("-"));
        version = version.substring(0, version.lastIndexOf("."));
        return Integer.parseInt(version);
    }
}
