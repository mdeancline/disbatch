package io.github.disbatch;

import io.github.disbatch.command.descriptor.CommandDescriptor;
import org.jetbrains.annotations.NotNull;

class DynamicUpdateRegistrar implements CommandRegistrar {
    private final CommandRegistrar source;

    DynamicUpdateRegistrar(CommandRegistrar source) {
        this.source = source;
    }

    @Override
    public void register(@NotNull CommandDescriptor<?, ?> descriptor) {
        source.register(descriptor);
        updateClientCommandLists();
    }

    @Override
    public void registerFromFile(@NotNull CommandDescriptor<?, ?> descriptor) {
        source.registerFromFile(descriptor);
        updateClientCommandLists();
    }

    private void updateClientCommandLists() {

    }
}
