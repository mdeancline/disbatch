package io.github.disbatch;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.descriptor.CommandTopic;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.exception.CommandException;
import io.github.disbatch.command.exception.CommandExecutionException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

class SimpleCommandRegistrar implements CommandRegistrar {
    private final CommandMap serverCommandMap;
    private final JavaPlugin plugin;

    SimpleCommandRegistrar(final JavaPlugin plugin) {
        try {
            this.plugin = plugin;
            final PluginManager pluginManager = plugin.getServer().getPluginManager();
            serverCommandMap = (CommandMap) getCommandMapField(pluginManager).get(pluginManager);
        } catch (final ReflectiveOperationException e) {
            throw new CommandException(e);
        }
    }

    private Field getCommandMapField(final PluginManager pluginManager) throws ReflectiveOperationException {
        final Class<? extends PluginManager> pluginManagerClass = pluginManager.getClass();
        final Stream<Field> stream = Arrays.stream(pluginManagerClass.getDeclaredFields())
                .filter(field -> CommandMap.class.isAssignableFrom(field.getType()));

        return extractCommandMapField(stream, pluginManagerClass);
    }

    private Field extractCommandMapField(final Stream<Field> stream, final Class<? extends PluginManager> pluginManagerClass) {
        if (stream.count() != 1)
            throw new CommandException("More than one CommandMap field found in " + pluginManagerClass.getName());

        final Optional<Field> optionalField = stream.findFirst();

        if (!optionalField.isPresent())
            throw new CommandException("CommandMap field from " + pluginManagerClass.getName() + " is null");

        return optionalField.get();
    }

    @Override
    public void register(final @NotNull Command<?> command, final @NotNull String label) {
        register(command, new CommandDescriptor.Builder().label(label).build());
    }

    @Override
    public void register(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor) {
        final String label = descriptor.getLabel();
        final CommandAdapter adapter = new CommandAdapter(command, descriptor);
        serverCommandMap.register(label, adapter);
        plugin.getServer().getHelpMap().addTopic(new CommandTopicAdapter(label, descriptor.getTopic()));
    }

    @Override
    public void registerFromFile(final @NotNull Command<?> command, final @NotNull String label) {
        registerFromFile(command, new CommandDescriptor.Builder().label(label).build());
    }

    @Override
    public void registerFromFile(final @NotNull Command<?> command, final @NotNull CommandDescriptor descriptor) {
        setupPluginCommandExecution(command, descriptor);
        plugin.getServer().getHelpMap().addTopic(new CommandTopicAdapter(descriptor.getLabel(), descriptor.getTopic()));
    }

    private void setupPluginCommandExecution(final Command<?> command, final CommandDescriptor descriptor) {
        final PluginCommand pluginCommand = getExistingPluginCommand(descriptor.getLabel());
        final CommandAdapter adapter = new CommandAdapter(command, descriptor);

        pluginCommand.setExecutor((sender, serverCommand, label, args) -> {
            adapter.execute(sender, label, args);
            return true;
        });

        pluginCommand.setTabCompleter((sender, serverCommand, label, args)
                -> new LinkedList<>(adapter.tabComplete(sender, label, args)));
    }

    private PluginCommand getExistingPluginCommand(final String commandLabel) {
        final PluginCommand pluginCommand = plugin.getCommand(commandLabel);

        if (pluginCommand == null)
            throw new CommandRegistrationException(String.format("Command \"%s\" is not registered in the plugin.yml file of %s",
                    commandLabel, plugin.getName()));

        return pluginCommand;
    }

    private static class CommandAdapter extends org.bukkit.command.Command {
        private final Command<CommandSender> command;
        private final CommandDescriptor descriptor;
        private final Class<?> senderType;

        @SuppressWarnings("unchecked")
        private CommandAdapter(final Command<?> command, final CommandDescriptor descriptor) {
            super(descriptor.getLabel());

            this.command = (Command<CommandSender>) command;
            this.descriptor = descriptor;
            senderType = extractSenderType(command);

            setAliases(descriptor.getAliases());
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
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
            if (sender == null)
                throw new CommandExecutionException("CommandSender is null");

            if (senderType.isAssignableFrom(sender.getClass()))
                command.execute(sender, computeInput(commandLabel, args));
            else if (!Strings.isNullOrEmpty(descriptor.getValidSenderMessage()))
                sender.sendMessage(descriptor.getValidSenderMessage());

            return true;
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) {
            return senderType.isAssignableFrom(sender.getClass())
                    ? new LinkedList<>(command.tabComplete(sender, computeInput(alias, args)))
                    : ImmutableList.of();
        }

        private CommandInput computeInput(final String label, final String[] args) {
            return args.length > 0 ? new LazyLoadingCommandInput(args, label) : new SingleLabelCommandInput(label);
        }

        @Override
        public String toString() {
            return command.toString();
        }
    }

    private static class CommandTopicAdapter extends HelpTopic {
        protected final CommandTopic adaptedTopic;

        private CommandTopicAdapter(final String name, final CommandTopic adaptedTopic) {
            this.adaptedTopic = adaptedTopic;
            this.name = "/" + name;
        }

        @Override
        public boolean canSee(final CommandSender sender) {
            return adaptedTopic.canSee(sender);
        }

        @Override
        public String getShortText() {
            return adaptedTopic.getShortText();
        }

        @Override
        public String getFullText(final CommandSender forWho) {
            return adaptedTopic.getFullText(forWho);
        }

        @Override
        public void amendTopic(final String amendedShortText, final String amendedFullText) {
            shortText = adaptedTopic.applyAmendment(shortText, amendedShortText);
            fullText = adaptedTopic.applyAmendment(fullText, amendedFullText);
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
            return StringUtils.EMPTY;
        }

        @Override
        public String[] getArguments() {
            return ArrayUtils.EMPTY_STRING_ARRAY;
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
