package io.github.disbatch;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @since 1.1.0
 */
public final class CommandRegistrars {
    private static final String BRIGADIER_PACKAGE_NAME = "com.mojang.brigadier";

    private static CommandRegistrar REGISTRAR;

    private CommandRegistrars() {
        throw new AssertionError();
    }

    /**
     *
     * @param plugin
     * @return
     */
    public static CommandRegistrar getCompatibleRegistrar(final @NotNull JavaPlugin plugin) {
        if (REGISTRAR == null) {
            final Package brigadierPackage = Package.getPackage(BRIGADIER_PACKAGE_NAME);

            return REGISTRAR = brigadierPackage == null
                    ? new SimpleCommandRegistrar(plugin)
                    : new BrigadierCommandRegistrar(plugin);
        }

        return REGISTRAR;
    }
}
