package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Parses a {@link ProxiedPlayer} by their name based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [player name]
 *
 * @since 1.0.0
 */
public final class PlayerFromNameParameter extends SenderIndependentParameter<ProxiedPlayer> {
    
    @Override
    protected @Nullable ProxiedPlayer parse(final CommandInput input) {
        final ProxyServer server = ProxyServer.getInstance();
        return server.getPlayer(input.getArgument(0));
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
