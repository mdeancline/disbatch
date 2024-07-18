package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.CommandInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Parses a {@link UUID} based on a parsable, passed argument.
 * <p>
 * <p>
 * <b>Syntax:</b> [uuid]
 *
 * @since 1.0.0
 */
public final class UUIDSyntax extends UUIDOrientedSyntax<CommandSender, UUID> {

    /**
     * Constructs a new {@code UUIDSyntax} with the specified argument label.
     */
    public UUIDSyntax(final @NotNull String label) {
        super(label);
    }

    @Override
    public boolean matches(CommandInput.Binding binding) {
        return isUniqueId(binding.getArgument());
    }

    @Override
    public boolean isGreedy() {
        return false;
    }

    @Override
    public @Nullable UUID parse(final CommandSender sender, final CommandInput input) {
        final String arg = input.getArgument(0);
        return isUniqueId(arg) ? UUID.fromString(arg) : null;
    }

    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }
}
