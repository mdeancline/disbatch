package io.github.disbatch;

import io.github.disbatch.command.Suggestion;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.descriptor.CommandTopic;
import io.github.disbatch.command.exception.CommandException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class BukkitCommandRegistrar implements CommandRegistrar {
    private final SimpleCommandMap serverCommandMap;
    private final Server server;

    BukkitCommandRegistrar(final Server server) {
        try {
            this.server = server;
            final PluginManager pluginManager = server.getPluginManager();
            serverCommandMap = (SimpleCommandMap) getCommandMapField(pluginManager).get(pluginManager);
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
            throw new CommandRegistrationException("More than one CommandMap field found in " + pluginManagerClass.getName());

        final Optional<Field> optionalField = stream.findFirst();

        if (!optionalField.isPresent())
            throw new CommandRegistrationException("CommandMap field from " + pluginManagerClass.getName() + " is null");

        return optionalField.get();
    }

    @Override
    public void register(final @NotNull CommandDescriptor<?, ?> descriptor) {
        final String label = descriptor.getLabel();
        final CommandAdapter adapter = new CommandAdapter(descriptor);
        serverCommandMap.register(label, adapter);
        server.getHelpMap().addTopic(new CommandTopicAdapter(descriptor));
    }

    @Override
    public void registerFromFile(final @NotNull CommandDescriptor<?, ?> descriptor) {
        setupPluginCommandExecution(descriptor);
        server.getHelpMap().addTopic(new CommandTopicAdapter(descriptor));
    }

    private void setupPluginCommandExecution(final CommandDescriptor<?, ?> descriptor) {
        final PluginCommand pluginCommand = getExistingPluginCommand(descriptor.getLabel());
        final CommandAdapter adapter = new CommandAdapter(descriptor);
        pluginCommand.setExecutor(adapter);
        pluginCommand.setTabCompleter(adapter);
    }

    private PluginCommand getExistingPluginCommand(final String commandLabel) {
        final PluginCommand pluginCommand = server.getPluginCommand(commandLabel);

        if (pluginCommand == null)
            throw new CommandRegistrationException(String.format("Command \"%s\" is not registered in the plugin.yml file of %s",
                    commandLabel, server.getName()));

        return pluginCommand;
    }

    static class CommandAdapter extends Command implements TabExecutor, CommandExecutor {
        private final CommandDescriptor<?, ?>.Command command;

        CommandAdapter(final CommandDescriptor<?, ?> descriptor) {
            super(descriptor.getLabel());
            command = descriptor.getCommand();
        }

        @Override
        public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
            return execute(sender, label, args);
        }

        @Override
        public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
            return tabComplete(sender, alias, args);
        }

        @Override
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] arguments) {
            command.execute(sender, commandLabel, arguments);
            return true;
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) {
            return Suggestion.toTexts(command.getSuggestions(sender, args));
        }
    }

    private static class CommandTopicAdapter extends HelpTopic {
        private final Class<?> senderType;
        private final CommandTopic<CommandSender> source;

        @SuppressWarnings("unchecked")
        private CommandTopicAdapter(final CommandDescriptor<?, ?> descriptor) {
            senderType = descriptor.getSenderType();
            source = (CommandTopic<CommandSender>) descriptor.getTopic();
            name = "/" + descriptor.getLabel();
        }

        @Override
        public boolean canSee(final CommandSender sender) {
            return senderType.isAssignableFrom(sender.getClass()) && source.isViewableTo(sender);
        }

        @Override
        public String getShortText() {
            return source.getShortText();
        }

        @Override
        public String getFullText(final CommandSender forWho) {
            return source.getFullText(forWho);
        }

        @Override
        public void amendTopic(final String amendedShortText, final String amendedFullText) {
            source.amend(amendedShortText, amendedFullText);
        }
    }
}
