package io.github.disbatch;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.decorator.CommandProxy;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

class TypedCommandProxy extends CommandProxy<CommandSender> {
    private final Class<?> senderType;
    private final String validSenderMessage;

    @SuppressWarnings("unchecked")
    TypedCommandProxy(final Command<?> innerCommand, final String validSenderMessage) {
        super((Command<CommandSender>) innerCommand);
        senderType = extractSenderType(innerCommand);
        this.validSenderMessage = validSenderMessage;
    }

    private Class<?> extractSenderType(final Command<?> command) {
        for (final TypeToken<?> type : TypeToken.of(command.getClass()).getTypes()) {
            if (type.getRawType().equals(Command.class)) {
                final Type typeArgument = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];

                if (typeArgument instanceof Class)
                    return (Class<?>) typeArgument;
            }
        }

        return CommandSender.class;
    }

    @Override
    public void execute(final CommandSender sender, final CommandInput input) {
        final String validSenderMessage = this.validSenderMessage;

        if (senderType.isAssignableFrom(sender.getClass()))
            super.execute(sender, input);
        else if (!Strings.isNullOrEmpty(validSenderMessage))
            sender.sendMessage(validSenderMessage);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final @NotNull CommandInput input) {
        return senderType.isAssignableFrom(sender.getClass())
                ? super.tabComplete(sender, input)
                : ImmutableList.of();
    }
}
