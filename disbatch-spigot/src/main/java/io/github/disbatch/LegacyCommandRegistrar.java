package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.CommandException;
import io.github.disbatch.command.exception.CommandExecutionException;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class LegacyCommandRegistrar implements CommandRegistrar {
    private final CommandMap serverCommandMap;
    private final Server server;

    LegacyCommandRegistrar(final Server server) {
        try {
            final PluginManager pluginManager = (this.server = server).getPluginManager();
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

    private static Field extractCommandMapField(final Stream<Field> stream, final Class<? extends PluginManager> pluginManagerClass) {
        if (stream.count() != 1)
            throw new CommandException("More than one CommandMap field found in " + pluginManagerClass.getName());

        final Optional<Field> optionalField = stream.findFirst();

        if (!optionalField.isPresent())
            throw new CommandException("CommandMap field from " + pluginManagerClass.getName() + " is null");

        return optionalField.get();
    }

    @Override
    public void register(final TypedCommandProxy typedCommand, final CommandDescriptor descriptor) {
        final String label = descriptor.getLabel();
        final CommandAdapter adapter = new CommandAdapter(typedCommand, descriptor);
        serverCommandMap.register(label, adapter);
        server.getHelpMap().addTopic(new CommandTopicAdapter(label, descriptor.getTopic()));
    }

    private static class CommandAdapter extends org.bukkit.command.Command {
        private final TypedCommandProxy typedCommand;

        private CommandAdapter(final TypedCommandProxy typedCommand, final CommandDescriptor descriptor) {
            super(descriptor.getLabel());
            this.typedCommand = typedCommand;

            setAliases(descriptor.getAliases());
        }

        @Override
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
            if (sender == null)
                throw new CommandExecutionException("CommandSender is null");

            typedCommand.execute(sender, computeInput(commandLabel, args));
            return true;
        }

        private CommandInput computeInput(final String label, final String[] args) {
            return args.length > 0 ? new LazyLoadingCommandInput(args, label) : new SingleLabelCommandInput(label);
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) {
            return typedCommand.tabComplete(sender, computeInput(alias, args));
        }

        @Override
        public String toString() {
            return typedCommand.toString();
        }
    }
}
