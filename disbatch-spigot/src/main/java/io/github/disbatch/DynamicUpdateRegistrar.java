package io.github.disbatch;

import io.github.disbatch.command.descriptor.CommandDescriptor;
import org.jetbrains.annotations.NotNull;

class DynamicUpdateRegistrar implements CommandRegistrar {
    private final CommandRegistrar source;

    DynamicUpdateRegistrar(CommandRegistrar source) {
        this.source = source;
    }

    @Override
    public void register(final @NotNull String label, final @NotNull CommandDescriptor descriptor) {
        source.register(label, descriptor);
        updateClientCommandLists();
    }

    @Override
    public void registerFromFile(final @NotNull String label, final @NotNull CommandDescriptor descriptor) {
        source.registerFromFile(label, descriptor);
        updateClientCommandLists();
    }

    private void updateClientCommandLists() {

    }
}
