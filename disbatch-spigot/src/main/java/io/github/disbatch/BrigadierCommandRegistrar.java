package io.github.disbatch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.disbatch.command.CommandInputs;
import io.github.disbatch.command.SimpleBinding;
import io.github.disbatch.command.Suggestion;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.exception.CommandRegistrationException;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.CommandSyntaxNodeIterator;
import io.github.disbatch.command.syntax.exception.ParseSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO complete development
class BrigadierCommandRegistrar implements CommandRegistrar {
    private static final String VERSION_TEXT = getMinecraftVersionText();
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?( .*)?\\)");

    private static final Field CUSTOM_SUGGESTIONS_FIELD;
    private static final Field COMMAND_EXECUTE_FUNCTION_FIELD;
    private static final Field CHILDREN_FIELD;
    private static final Field LITERALS_FIELD;
    private static final Field ARGUMENTS_FIELD;
    private static final Field[] CHILDREN_FIELDS;
    private static final com.mojang.brigadier.Command<?> DUMMY_COMMAND;
    private static final SuggestionProvider<?> DUMMY_SUGGESTION_PROVIDER;


    private static final Field CONSOLE_FIELD;
    private static final Method GET_NATIVE_DISPATCHER_METHOD;
    private static final Method GET_BRIGADIER_DISPATCHER_METHOD;
    private static final Constructor<?> BUKKIT_COMMAND_WRAPPER_CONSTRUCTOR;
    private static final CommandDispatcher<Object> BRIGADIER_DISPATCHER;

    private final List<LiteralCommandNode<?>> nodes = new ArrayList<>();
    private final Server server;

    BrigadierCommandRegistrar(final Server server) {
        this.server = server;
    }

    static {
        try {
            final Class<?> minecraftServer;
            final Class<?> nativeCommandDispatcher;

            if (getMinecraftVersion() > 16) {
                minecraftServer = asMinecraftClass("MinecraftServer");
                nativeCommandDispatcher = asMinecraftClass("commands.CommandDispatcher");
            } else {
                minecraftServer = asLegacyMinecraftClass("MinecraftServer");
                nativeCommandDispatcher = asLegacyMinecraftClass("CommandDispatcher");
            }

            CUSTOM_SUGGESTIONS_FIELD = ArgumentCommandNode.class.getDeclaredField("customSuggestions");
            CUSTOM_SUGGESTIONS_FIELD.setAccessible(true);

            COMMAND_EXECUTE_FUNCTION_FIELD = CommandNode.class.getDeclaredField("command");
            COMMAND_EXECUTE_FUNCTION_FIELD.setAccessible(true);

            CHILDREN_FIELD = CommandNode.class.getDeclaredField("children");
            LITERALS_FIELD = CommandNode.class.getDeclaredField("literals");
            ARGUMENTS_FIELD = CommandNode.class.getDeclaredField("arguments");
            CHILDREN_FIELDS = new Field[] {CHILDREN_FIELD, LITERALS_FIELD, ARGUMENTS_FIELD};

            for (Field field : CHILDREN_FIELDS)
                field.setAccessible(true);

            DUMMY_COMMAND = (context) -> { throw new UnsupportedOperationException(); };
            DUMMY_SUGGESTION_PROVIDER = (context, builder) -> { throw new UnsupportedOperationException(); };

            final Class<?> craftServerClass = asCraftBukkitClass("CraftServer");
            CONSOLE_FIELD = craftServerClass.getDeclaredField("console");
            CONSOLE_FIELD.setAccessible(true);

            GET_NATIVE_DISPATCHER_METHOD = Arrays.stream(minecraftServer.getDeclaredMethods())
                    .filter(method -> method.getParameterCount() == 0)
                    .filter(method -> nativeCommandDispatcher.isAssignableFrom(method.getReturnType()))
                    .findFirst().orElseThrow(NoSuchMethodException::new);
            GET_NATIVE_DISPATCHER_METHOD.setAccessible(true);

            GET_BRIGADIER_DISPATCHER_METHOD = Arrays.stream(nativeCommandDispatcher.getDeclaredMethods())
                    .filter(method -> method.getParameterCount() == 0)
                    .filter(method -> CommandDispatcher.class.isAssignableFrom(method.getReturnType()))
                    .findFirst().orElseThrow(NoSuchMethodException::new);
            GET_BRIGADIER_DISPATCHER_METHOD.setAccessible(true);

            final Class<?> bukkitCommandWrapperClass = asCraftBukkitClass("command.BukkitCommandWrapper");
            BUKKIT_COMMAND_WRAPPER_CONSTRUCTOR = bukkitCommandWrapperClass.getConstructor(craftServerClass, org.bukkit.command.Command.class);

            final Object mcServerObject = CONSOLE_FIELD.get(Bukkit.getServer());
            final Object commandDispatcherObject = GET_NATIVE_DISPATCHER_METHOD.invoke(mcServerObject);
            BRIGADIER_DISPATCHER = (CommandDispatcher) GET_BRIGADIER_DISPATCHER_METHOD.invoke(commandDispatcherObject);

        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void setHackyFields(CommandNode<?> node, SuggestionProvider suggestionProvider) {
        try {
            COMMAND_EXECUTE_FUNCTION_FIELD.set(node, DUMMY_COMMAND);

            if (suggestionProvider != null && node instanceof ArgumentCommandNode) {
                ArgumentCommandNode<?, ?> argumentNode = (ArgumentCommandNode<?, ?>) node;
                CUSTOM_SUGGESTIONS_FIELD.set(argumentNode, suggestionProvider);
            }

            for (final CommandNode<?> child : node.getChildren()) {
                setHackyFields(child, suggestionProvider);
            }
        } catch (final IllegalAccessException e) {
            throw new CommandRegistrationException(e);
        }
    }

    private static int getMinecraftVersion() {
        final Matcher matcher = VERSION_PATTERN.matcher(Bukkit.getVersion());

        if (matcher.find()) {
            return Integer.parseInt(matcher.toMatchResult().group(2), 10);
        } else {
            throw new CommandRegistrationException("Unable to extract the correct Minecraft version");
        }
    }

    private static String getMinecraftVersionText() {
        Class<?> server = Bukkit.getServer().getClass();

        if (!server.getSimpleName().equals("CraftServer"))
            return ".";

        if (server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            return ".";
        } else {
            final String version = server.getName().substring("org.bukkit.craftbukkit".length());
            return version.substring(0, version.length() - "CraftServer".length());
        }
    }

    private static Class<?> asMinecraftClass(final String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + name);
    }

    private static Class<?> asLegacyMinecraftClass(final String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server" + VERSION_TEXT + name);
    }

    private static Class<?> asCraftBukkitClass(final String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit" + VERSION_TEXT + name);
    }

    @Override
    public void register(final @NotNull CommandDescriptor<?,?> descriptor) {
        setupBrigadierCommandExecution(descriptor);
    }

    @Override
    public void registerFromFile(final @NotNull CommandDescriptor<?,?> descriptor) {
    }

    @SuppressWarnings("unchecked")
    private void setupBrigadierCommandExecution(final CommandDescriptor<?, ?> descriptor) {
        try {
            final SuggestionProvider<Object> suggestionWrapper = (SuggestionProvider<Object>) BUKKIT_COMMAND_WRAPPER_CONSTRUCTOR.newInstance(server, new BukkitCommandRegistrar.CommandAdapter(descriptor));
            final RootCommandNode<Object> rootNode = BRIGADIER_DISPATCHER.getRoot();
            final LiteralArgumentBuilder<Object> builder = LiteralArgumentBuilder.literal(descriptor.getLabel());
//            populateLiteral(builder, syntax);

            BRIGADIER_DISPATCHER.register(builder);

        } catch (final ReflectiveOperationException e) {
            throw new CommandRegistrationException(e);
        }
    }

    private void populateLiteral(final LiteralArgumentBuilder<Object> builder, final CommandSyntaxNodeIterator iterator) {
        while (iterator.hasNext())
            visit(iterator.next(), builder);
    }

    private void visit(final CommandSyntax.Node node, final ArgumentBuilder<Object, ?> builder) {
        if (node.hasChildren()) {
            for (final CommandSyntax.Node child : node) {
                final RequiredArgumentBuilder<Object, ?> childBuilder = RequiredArgumentBuilder.argument(child.getLabel(), null);
                builder.then(childBuilder);
                visit(child, childBuilder);
            }
        } else {
            builder.then(LiteralArgumentBuilder.literal(node.getLabel()));
        }
    }

    private static class CommandAdapter extends CommandNode<CommandSender> {
        private final CommandSyntax<CommandSender, Object> syntax;
        private final String name;

        @SuppressWarnings("unchecked")
        private CommandAdapter(final CommandDescriptor<?, ?> descriptor) {
            super(
                    createCommand(descriptor),
                    sender -> true,
                    null,
                    context -> Collections.singleton(context.getSource()),
                    true
            );

            syntax = (CommandSyntax<CommandSender, Object>) descriptor.getSyntax();
            name = descriptor.getLabel();
        }

        @SuppressWarnings("unchecked")
        private static Command<CommandSender> createCommand(final CommandDescriptor<?, ?> descriptor) {
            final CommandDescriptor<CommandSender, Object>.Command command = (CommandDescriptor<CommandSender, Object>.Command) descriptor.getCommand();

            return context -> {
                final String[] input = context.getInput().split(" ");
                command.execute(context.getSource(), CommandInputs.of(input[0], input, descriptor.getSyntax()));
                return Command.SINGLE_SUCCESS;
            };
        }

        @Override
        protected boolean isValidInput(final String input) {
            if (input.length() > syntax.getMaximumUsage()) return false;

            final int index = input.length() - 1;
            final CommandSyntax.Node node = syntax.getNode(index);
            final String[] arguments = input.substring(0, input.indexOf(" ")).split(" ");
            return syntax.matches(new SimpleBinding(node, arguments, index));
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getUsageText() {
            return "";
        }

        @Override
        public void parse(StringReader reader, CommandContextBuilder<CommandSender> contextBuilder) throws CommandSyntaxException {
            throw new ParseSyntaxException("Illegal parsing call from Brigadier command");
        }

        @Override
        public CompletableFuture<Suggestions> listSuggestions(final CommandContext<CommandSender> context, final SuggestionsBuilder builder) {
            final Collection<Suggestion> suggestions = syntax.getSuggestions(context.getSource(), context.getInput().split(" "));

            for (final Suggestion suggestion : suggestions)
                builder.suggest(suggestion.getText(), new LiteralMessage(suggestion.getTooltip()));

            return builder.buildFuture();
        }

        @Override
        public ArgumentBuilder<CommandSender, ?> createBuilder() {
            return null;
        }

        @Override
        protected String getSortedKey() {
            return name;
        }

        @Override
        public Collection<String> getExamples() {
            return null;
        }
    }
}
