package io.github.disbatch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.disbatch.command.CommandRegistration;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

abstract class BrigadierCommandRegistrar implements CommandRegistrar {
    private final CommandDispatcher<Sender> dispatcher;

    @SuppressWarnings("unchecked")
    BrigadierCommandRegistrar(final Server server) {
        dispatcher = (CommandDispatcher<Sender>) getDispatcher(server);
    }

    @Override
    public final void register(@NotNull final CommandRegistration registration) {
        setupBrigadierCommandExecution(registration);
    }

    @Override
    public final void registerFromFile(@NotNull final CommandRegistration registration) {
    }

    private void setupBrigadierCommandExecution(final CommandRegistration registration) {
        final CommandAdapter adapter = new CommandAdapter(registration);
        final CommandSyntax<?, ?> syntax = registration.getSyntax();
        final LiteralArgumentBuilder<Sender> builder = LiteralArgumentBuilder.<Sender>literal(registration.getLabel())
                .requires(adapter)
                .executes(adapter);

        for (final CommandSyntax.Literal literal : registration.getSyntax())
            builder.then(visit(literal, syntax));

        dispatcher.register(builder);
    }

    protected abstract CommandDispatcher<?> getDispatcher(Server server);

    private <V> RequiredArgumentBuilder<Sender, V> visit(final CommandSyntax.Literal literal, final CommandSyntax<?, V> syntax) {
        final Collection<CommandSyntax.Literal> children = literal.getChildren();
        if (children.size() > 0) {

        }

        return RequiredArgumentBuilder.argument(literal.getLabel(), new CommandSyntaxArgumentType<>(syntax));
    }

    interface Sender {
        CommandSender getBukkitSender();
    }

    private static class CommandAdapter implements Command<Sender>, Predicate<Sender> {
        private final CommandRegistration.Command command;
        private final CommandSyntax<CommandSender, Object> syntax;
        private final Permission permission;

        @SuppressWarnings("unchecked")
        private CommandAdapter(final CommandRegistration registration) {
            command = registration.getCommand();
            permission = registration.getPermission();
            syntax = (CommandSyntax<CommandSender, Object>) registration.getSyntax();
        }

//        @Override
//        protected boolean isValidInput(final String input) {
//            if (input.length() > syntax.getMaximumUsage()) return false;
//
//            final int index = input.length() - 1;
//            final CommandSyntax.Literal literal = syntax.getLiteral(index);
//            final String[] arguments = input.substring(0, input.indexOf(" ")).split(" ");
//            return syntax.matches(new SimpleBinding(literal, arguments, index));
//        }

        @Override
        public int run(final CommandContext<Sender> context) {
            final String[] input = context.getInput().split(" ");
            final String[] arguments = new String[input.length - 1];
            final CommandSender sender = context.getSource().getBukkitSender();
            System.arraycopy(input, 1, arguments, 0, arguments.length);
            command.execute(sender, input[0], arguments);

            return Command.SINGLE_SUCCESS;
        }

//        @Override
//        public CompletableFuture<Suggestions> getSuggestions(final CommandContext<Sender> context, final SuggestionsBuilder builder) {
//            final CommandSender sender = context.getSource().getBukkitSender();
//            final String[] input = context.getInput().split(" ");
//            final Collection<Suggestion> suggestions = syntax.getSuggestions(sender, input);
//
//            for (final Suggestion suggestion : suggestions)
//                builder.suggest(suggestion.getText(), new LiteralMessage(suggestion.getTooltip()));
//
//            return builder.buildFuture();
//        }

        @Override
        public boolean test(final Sender sender) {
            if (permission == null) return true;
            return sender.getBukkitSender().hasPermission(permission);
        }
    }

    private static class CommandSyntaxArgumentType<V> implements ArgumentType<V> {
        private final CommandSyntax<?, V> syntax;

        private CommandSyntaxArgumentType(final CommandSyntax<?, V> syntax) {
            this.syntax = syntax;
        }

        @Override
        public V parse(final StringReader stringReader) {
            return null;
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
            return ArgumentType.super.listSuggestions(commandContext, suggestionsBuilder);
        }

        @Override
        public Collection<String> getExamples() {
            return ArgumentType.super.getExamples();
        }
    }
}
