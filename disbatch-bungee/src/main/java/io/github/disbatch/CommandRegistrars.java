package io.github.disbatch;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @since 1.0.0
 */
public final class CommandRegistrars {
    private static CommandRegistrar REGISTRAR;

    private CommandRegistrars() {
        throw new AssertionError();
    }

    /**
     *
     * @param plugin
     * @return
     */
    public static CommandRegistrar getCompatibleRegistrar(final @NotNull Plugin plugin) {
        if (REGISTRAR == null) {
            return REGISTRAR = new SimpleCommandRegistrar(plugin);
        }

        return REGISTRAR;
    }
}
