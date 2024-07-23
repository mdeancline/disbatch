package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;

public interface SyntaxExecutor<S extends CommandSender, V> extends CommandExecutor<S, V> {
    CommandSyntax<? super S, V> getSyntax();
}
