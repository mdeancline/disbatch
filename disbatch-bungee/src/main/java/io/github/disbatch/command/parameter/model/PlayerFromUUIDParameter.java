package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses a {@link ProxiedPlayer} by their {@link UUID} based on a parsable, passed argument.
 * <p>
 * <p>
 * <b>Argument Syntax:</b> [uuid]
 *
 * @since 1.0.0
 */
public final class PlayerFromUUIDParameter extends UUIDOrientedParameter<CommandSender, ProxiedPlayer> {
    
    @Override
    public @Nullable ProxiedPlayer parse(final CommandSender sender, final CommandInput input) {
        final ProxyServer server = ProxyServer.getInstance();
        final String arg = input.getArgument(0);

        return isUniqueId(arg)
                ? server.getPlayer(UUID.fromString(arg))
                : null;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }
}
