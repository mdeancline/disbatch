package io.github.disbatch;

import io.github.disbatch.command.CommandTopic;
import io.github.disbatch.command.Suggestion;
import io.github.disbatch.command.exception.CommandException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.bukkit.Bukkit;
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

    BukkitCommandRegistrar() {
        try {
            final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
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
    public void register(@NotNull final Command command) {
        final CommandAdapter adapter = new CommandAdapter(command);
        serverCommandMap.register(adapter.getLabel(), adapter);
        Bukkit.getHelpMap().addTopic(new CommandTopicAdapter(command));
    }

    @Override
    public void registerFromFile(@NotNull final Command command) {
        setupPluginCommandExecution(command);
        Bukkit.getHelpMap().addTopic(new CommandTopicAdapter(command));
    }

    private void setupPluginCommandExecution(final Command registration) {
        final PluginCommand pluginCommand = getExistingPluginCommand(registration);
        final CommandAdapter adapter = new CommandAdapter(registration);
        pluginCommand.setExecutor(adapter);
        pluginCommand.setTabCompleter(adapter);
    }

    private PluginCommand getExistingPluginCommand(final Command registration) {
        final PluginCommand pluginCommand = Bukkit.getPluginCommand(registration.getLabel());

        if (pluginCommand == null)
            throw new CommandRegistrationException(String.format("Command \"%s\" is not registered in the plugin.yml file of %s",
                    registration.getLabel(), Bukkit.getName()));

        return pluginCommand;
    }

    static class CommandAdapter extends org.bukkit.command.Command implements TabExecutor, CommandExecutor {
        private final Command.Executable command;

        CommandAdapter(final io.github.disbatch.Command registration) {
            super(registration.getLabel());
            command = registration.getExecutable();
        }

        @Override
        public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args) {
            return execute(sender, label, args);
        }

        @Override
        public List<String> onTabComplete(final CommandSender sender, final org.bukkit.command.Command command, final String alias, final String[] args) {
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
        private CommandTopicAdapter(final Command registration) {
            senderType = registration.getSenderType();
            source = (CommandTopic<CommandSender>) registration.getTopic();
            name = "/" + registration.getLabel();
        }

        @Override
        public boolean canSee(final CommandSender sender) {
            return senderType.isAssignableFrom(sender.getClass()) && source.canSee(sender);
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
