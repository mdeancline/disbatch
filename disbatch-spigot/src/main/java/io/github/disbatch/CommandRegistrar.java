package io.github.disbatch;

import io.github.disbatch.command.descriptor.CommandDescriptor;

interface CommandRegistrar {
    void register(TypedCommandProxy typedCommand, CommandDescriptor descriptor);
}
