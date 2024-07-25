package io.github.disbatch.command.decorator;

import com.google.common.base.Strings;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.StringJoiner;

/**
 * A {@link CommandProxy} that is fully executed for any {@link CommandSender} having a specific permission node.
 *
 * @param <S> {@inheritDoc}
 * @since 1.0.0
 * @deprecated
 */
@Deprecated
public final class PermissibleCommand<S extends CommandSender> extends CommandProxy<S> {
    private final String permissionNode;
    private final String noPermissionMessage;

    public PermissibleCommand(@NotNull final Command<S> innerCommand, @NotNull final String permissionNode, final String noPermissionMessage) {
        super(innerCommand);
        this.permissionNode = permissionNode;
        this.noPermissionMessage = noPermissionMessage;
    }

    @Override
    public void execute(final S sender, @NotNull final CommandInput input) {
        if (sender.hasPermission(permissionNode))
            super.execute(sender, input);
        else if (!Strings.isNullOrEmpty(noPermissionMessage))
            sender.sendMessage(noPermissionMessage.replace("%permission", permissionNode));
    }

    @Override
    public Collection<String> tabComplete(final S sender, @NotNull final CommandInput input) {
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