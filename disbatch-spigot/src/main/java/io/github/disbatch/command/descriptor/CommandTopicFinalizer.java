package io.github.disbatch.command.descriptor;

interface CommandTopicFinalizer<T extends CommandTopic> {
    CommandTopic finalize(T topic, CommandDescriptor descriptor);
}
