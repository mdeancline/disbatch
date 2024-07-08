package io.github.disbatch.mock;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.ConsoleCommandSender;

public class ConsoleCommand implements Command<ConsoleCommandSender> {
    @Override
    public void execute(final ConsoleCommandSender sender, final CommandInput input) {
        sender.sendMessage("You, the console, executed this command");
    }
}
