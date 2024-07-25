package io.github.disbatch;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ClientCommandsUpdateRegistrar implements CommandRegistrar {
    private final CommandRegistrar source;

    @Override
    public void register(@NotNull final Command command) {
        source.register(command);
        updateCommands();
    }

    private void updateCommands() {
        for (final Player player : Bukkit.getOnlinePlayers())
            player.updateCommands();
    }
}
