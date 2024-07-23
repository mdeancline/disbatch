package io.github.disbatch;

import io.github.disbatch.command.CommandRegistration;
import io.github.disbatch.command.CommandTopic;
import io.github.disbatch.command.Suggestion;
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
    public void register(@NotNull final CommandRegistration registration) {
        final CommandAdapter adapter = new CommandAdapter(registration);
        serverCommandMap.register(adapter.getLabel(), adapter);
        server.getHelpMap().addTopic(new CommandTopicAdapter(registration));
    }

    @Override
    public void registerFromFile(@NotNull final CommandRegistration registration) {
        setupPluginCommandExecution(registration);
        server.getHelpMap().addTopic(new CommandTopicAdapter(registration));
    }

    private void setupPluginCommandExecution(final CommandRegistration registration) {
        final PluginCommand pluginCommand = getExistingPluginCommand(registration);
        final CommandAdapter adapter = new CommandAdapter(registration);
        pluginCommand.setExecutor(adapter);
        pluginCommand.setTabCompleter(adapter);
    }

    private PluginCommand getExistingPluginCommand(final CommandRegistration registration) {
        final PluginCommand pluginCommand = server.getPluginCommand(registration.getLabel());

        if (pluginCommand == null)
            throw new CommandRegistrationException(String.format("Command \"%s\" is not registered in the plugin.yml file of %s",
                    registration.getLabel(), server.getName()));

        return pluginCommand;
    }

    static class CommandAdapter extends Command implements TabExecutor, CommandExecutor {
        private final CommandRegistration.Command command;

        CommandAdapter(final CommandRegistration registration) {
            super(registration.getLabel());
            command = registration.getCommand();
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
        private CommandTopicAdapter(final CommandRegistration registration) {
            senderType = registration.getSenderType();
            source = (CommandTopic<CommandSender>) registration.getTopic();
            name = "/" + registration.getLabel();
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
