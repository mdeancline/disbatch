package io.github.disbatch.command.decorator;

import com.google.common.base.Strings;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * A {@link CommandProxy} that is fully executed for any {@link CommandSender} having a specific permission node.
 *
 * @param <S> {@inheritDoc}
 *
 * @since 1.0.0
 */
public final class PermissibleCommand<S extends CommandSender> extends CommandProxy<S> {
    private final String permissionNode;
    private final String noPermissionMessage;

    public PermissibleCommand(final @NotNull Command<S> innerCommand, final @NotNull String permissionNode, final String noPermissionMessage) {
        super(innerCommand);
        this.permissionNode = permissionNode;
        this.noPermissionMessage = noPermissionMessage;
    }

    @Override
    public void execute(final S sender, final @NotNull CommandInput input) {
        if (sender.hasPermission(permissionNode))
            super.execute(sender, input);
        else if (!Strings.isNullOrEmpty(noPermissionMessage))
            sender.sendMessage(noPermissionMessage.replace("%permission", permissionNode));
    }

    @Override
    public Collection<String> tabComplete(final S sender, final @NotNull CommandInput input) {
        return sender.hasPermission(permissionNode)
                ? super.tabComplete(sender, input)
                : Collections.emptyList();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("requiredPermissionNode='" + permissionNode + "'")
                .add("innerCommand=" + super.toString())
                .toString();
    }
}
