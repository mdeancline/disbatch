package io.github.disbatch;

import io.github.disbatch.command.CommandTopic;
import io.github.disbatch.command.SimpleBinding;
import io.github.disbatch.command.exception.CommandException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import io.github.disbatch.command.syntax.CommandLiteral;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.Suggestion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class BukkitCommandRegistrar implements CommandRegistrar {
    private final SimpleCommandMap serverCommandMap;

    BukkitCommandRegistrar() {
        try {
            final PluginManager pluginManager = Bukkit.getPluginManager();
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

    private static class CommandAdapter extends org.bukkit.command.Command {
        private final Command.Executable executable;
        private final CommandSyntax<CommandSender, ?> syntax;

        private CommandAdapter(final Command command) {
            super(command.getLabel());
            executable = command.getExecutable();
            syntax = command.getSyntax();
        }

        @Override
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] arguments) {
            executable.execute(sender, commandLabel, arguments);
            return true;
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) {
            final int index = args.length - 1;
            final CommandLiteral literal = syntax.getLiteral(index);
            if (literal == null) return Collections.emptyList();

            return Suggestion.toTexts(executable.getSuggestions(sender, new SimpleBinding(literal, args, index)));
        }
    }

    private static class CommandTopicAdapter extends HelpTopic {
        private final Class<?> senderType;
        private final CommandTopic<CommandSender> source;

        private CommandTopicAdapter(final Command registration) {
            senderType = registration.getSenderType();
            source = registration.getTopic();
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
