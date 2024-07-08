package io.github.disbatch;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.ConsoleCommandSender;
import io.github.disbatch.command.decorator.CommandProxy;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.exception.CommandExecutionException;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

class SimpleCommandRegistrar implements CommandRegistrar {
    private final Plugin plugin;

    SimpleCommandRegistrar(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(@NotNull Command<?> command, @NotNull String label) {
        register(command, new CommandDescriptor.Builder().label(label).build());
    }

    @Override
    public void register(@NotNull Command<?> command, @NotNull CommandDescriptor descriptor) {
        final TypedCommandProxy commandProxy = new TypedCommandProxy(command, descriptor.getValidSenderMessage());
        final CommandAdapter adapter = new CommandAdapter(commandProxy, descriptor);
        plugin.getProxy().getPluginManager().registerCommand(plugin, adapter);
    }

    private static class CommandAdapter extends net.md_5.bungee.api.plugin.Command implements TabExecutor {
        private static final String BUNGEE_CONSOLE_CLASS_NAME = "net.md_5.bungee.command.ConsoleCommandSender";

        private final TypedCommandProxy typedCommand;

        private CommandAdapter(final TypedCommandProxy typedCommand, final CommandDescriptor descriptor) {
            super(descriptor.getLabel(), null, descriptor.getAliases());
            this.typedCommand = typedCommand;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (sender == null)
                throw new CommandExecutionException("CommandSender is null");

            typedCommand.execute(checkForConsole(sender), computeInput(getName(), args));
        }

        private CommandSender checkForConsole(final CommandSender sender) {
            if (sender.getClass().getName().equals(BUNGEE_CONSOLE_CLASS_NAME))
                return new ConsoleAdapter(sender);

            return sender;
        }

        private CommandInput computeInput(final String label, final String[] args) {
            return args.length > 0 ? new LazyLoadingCommandInput(args, label) : new SingleLabelCommandInput(label);
        }

        @Override
        public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
            return typedCommand.tabComplete(sender, computeInput("", args));
        }

        @Override
        public String toString() {
            return typedCommand.toString();
        }
    }

    private static class ConsoleAdapter implements ConsoleCommandSender {
        private final CommandSender consoleSender;

        private ConsoleAdapter(final CommandSender consoleSender) {
            this.consoleSender = consoleSender;
        }

        @Override
        public String getName() {
            return consoleSender.getName();
        }

        @Override
        public void sendMessage(String message) {
            consoleSender.sendMessage(message);
        }

        @Override
        public void sendMessages(String... messages) {
            consoleSender.sendMessages(messages);
        }

        @Override
        public void sendMessage(BaseComponent... message) {
            consoleSender.sendMessage(message);
        }

        @Override
        public void sendMessage(BaseComponent message) {
            consoleSender.sendMessage(message);
        }

        @Override
        public Collection<String> getGroups() {
            return consoleSender.getGroups();
        }

        @Override
        public void addGroups(String... groups) {
            consoleSender.addGroups(groups);
        }

        @Override
        public void removeGroups(String... groups) {
            consoleSender.removeGroups(groups);
        }

        @Override
        public boolean hasPermission(String permission) {
            return consoleSender.hasPermission(permission);
        }

        @Override
        public void setPermission(String permission, boolean value) {
            consoleSender.setPermission(permission, value);
        }

        @Override
        public Collection<String> getPermissions() {
            return consoleSender.getPermissions();
        }
    }

    private static class TypedCommandProxy extends CommandProxy<CommandSender> {
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
        public Collection<String> tabComplete(final CommandSender sender, final @NotNull CommandInput input) {
            return senderType.isAssignableFrom(sender.getClass())
                    ? super.tabComplete(sender, input)
                    : ImmutableList.of();
        }
    }

    static class SingleLabelCommandInput implements CommandInput {
        private final String label;

        SingleLabelCommandInput(final String label) {
            this.label = label;
        }

        @Override
        public int getArgumentLength() {
            return 0;
        }

        @Override
        public String getArgumentLine() {
            return emptyString();
        }

        @Override
        public String getArgument(final int index) {
            return emptyString();
        }

        private String emptyString() {
            return "";
        }

        @Override
        public String[] getArguments() {
            return new String[]{} ;
        }

        @Override
        public String getCommandLabel() {
            return label;
        }

        @Override
        public String getCommandLine() {
            return label;
        }
    }

    static class LazyLoadingCommandInput implements CommandInput {
        private final String[] arguments;
        private final String cmdLabel;
        private String argumentLine;
        private String commandLine;

        LazyLoadingCommandInput(final String[] arguments, final String cmdLabel) {
            this.arguments = arguments;
            this.cmdLabel = cmdLabel;
        }

        @Override
        public String getArgument(final int index) {
            if (index < 0 || index >= arguments.length)
                throw new ArgumentIndexOutOfBoundsException(index);

            return arguments[index];
        }

        @Override
        public String[] getArguments() {
            return arguments;
        }

        @Override
        public String getCommandLabel() {
            return cmdLabel;
        }

        @Override
        public int getArgumentLength() {
            return arguments.length;
        }

        @Override
        public String getArgumentLine() {
            if (argumentLine == null)
                return argumentLine = arguments.length == 1
                        ? arguments[0]
                        : String.join(" ", arguments);

            return argumentLine;
        }

        @Override
        public String getCommandLine() {
            return commandLine == null
                    ? (commandLine = String.join(" ", cmdLabel, getArgumentLine()))
                    : commandLine;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof CommandInput)) return false;
            final CommandInput that = (CommandInput) o;
            return getCommandLine().equals(that.getCommandLine());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCommandLine());
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                    .add("commandLine=" + getCommandLine())
                    .toString();
        }
    }
}
