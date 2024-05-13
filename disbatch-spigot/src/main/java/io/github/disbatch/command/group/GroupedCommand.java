package io.github.disbatch.command.group;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class GroupedCommand<S extends CommandSender> {
    private final Command<S> innerCommand;
    private final String label;

    GroupedCommand(final @NotNull Command<S> innerCommand, final String label) {
        this.innerCommand = innerCommand;
        this.label = label;
    }

    void execute(final S sender, final CommandInput input) {
        innerCommand.execute(sender, input);
    }

    String getLabel() {
        return label;
    }
}
