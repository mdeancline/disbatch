package io.github.disbatch;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.disbatch.command.SimpleBinding;
import io.github.disbatch.command.exception.CommandRegistrationException;
import io.github.disbatch.command.syntax.CommandLiteral;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.Suggestion;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

class BrigadierCommandRegistrar implements CommandRegistrar {
    private static final ArgumentType<String> EMPTY_TYPE = StringReader::getRead;

    private final BrigadierRegistry registry;

    BrigadierCommandRegistrar(final BrigadierRegistryContext context) {
        registry = context.getRegistry();
    }

    @Override
    public void register(@NotNull final Command command) {
        final CommandAdapter adapter = new CommandAdapter(command);
        final CommandAdapterBuilder builder = new CommandAdapterBuilder(command).executes(adapter).requires(adapter);
        final CommandLiteral literal = command.getLiteral(0);

        if (literal != null) visit(literal, builder);

        registry.register(builder);
    }

    private void visit(final CommandLiteral literal, final ArgumentBuilder<CommandSender, ?> builder) {
        for (final CommandLiteral child : literal) {
            final RequiredArgumentBuilder<CommandSender, String> childArgBuilder = RequiredArgumentBuilder.argument(child.getLabel(), EMPTY_TYPE);
            builder.then(childArgBuilder);
            visit(child, childArgBuilder);
        }
    }

    private static class CommandAdapterBuilder extends LiteralArgumentBuilder<CommandSender> {
        private final Command command;

        private CommandAdapterBuilder(final Command command) {
            super(command.getLabel());
            this.command = command;
        }

        @Override
        public CommandAdapterBuilder executes(final com.mojang.brigadier.Command<CommandSender> command) {
            super.executes(command);
            return this;
        }

        @Override
        public CommandAdapterBuilder requires(final Predicate<CommandSender> requirement) {
            super.requires(requirement);
            return this;
        }

        @Override
        public LiteralCommandNode<CommandSender> build() {
            return new CommandNodeAdapter(super.build(), command);
        }
    }

    private static class CommandNodeAdapter extends LiteralCommandNode<CommandSender> {
        private final LiteralCommandNode<CommandSender> source;
        private final CommandSyntax<CommandSender, ?> syntax;
        private final String label;

        private CommandNodeAdapter(final LiteralCommandNode<CommandSender> source, final Command command) {
            super(command.getLabel(), source.getCommand(), source.getRequirement(), source.getRedirect(), source.getRedirectModifier(), source.isFork());
            this.source = source;
            this.syntax = command.getSyntax();
            label = command.getLabel();
        }

        @Override
        public boolean isValidInput(final String input) {
            if (input.length() > syntax.getMaximumUsage()) return false;

            final int index = input.length() - 1;
            final CommandLiteral literal = syntax.getLiteral(index);
            if (literal == null) return true;

            final String[] arguments = input.substring(0, input.indexOf(" ")).split(" ");
            return syntax.matches(new SimpleBinding(literal, arguments, index));
        }

        @Override
        public String getName() {
            return source.getName();
        }

        @Override
        public String getUsageText() {
            return source.getUsageText();
        }

        @Override
        public void parse(final StringReader reader, final CommandContextBuilder<CommandSender> builder) throws CommandSyntaxException {
            source.parse(reader, builder);
        }

        @Override
        public CompletableFuture<Suggestions> listSuggestions(final CommandContext<CommandSender> context, final SuggestionsBuilder builder) {
            final String input = context.getInput();
            final int index = input.length() - 2;
            final CommandLiteral literal = syntax.getLiteral(index);
            if (literal == null) return builder.buildFuture();

            final String[] arguments = input.substring(1, input.length() - 1).split(" ");
            final Collection<Suggestion> suggestions = syntax.getSuggestions(context.getSource(), new SimpleBinding(literal, arguments, index));

            for (final Suggestion suggestion : suggestions)
                builder.suggest(suggestion.getText(), new LiteralMessage(suggestion.getTooltip()));

            return builder.buildFuture();
        }

        @Override
        public CommandAdapterBuilder createBuilder() {
            throw new CommandRegistrationException("Illegal builder call");
        }

        @Override
        protected String getSortedKey() {
            return label;
        }

        @Override
        public Collection<String> getExamples() {
            return source.getExamples();
        }
    }

    private static class CommandAdapter implements com.mojang.brigadier.Command<CommandSender>, Predicate<CommandSender> {
        private final Command.Executable executable;
        private final Permission permission;

        private CommandAdapter(final Command command) {
            executable = command.getExecutable();
            permission = command.getPermission();
        }

        @Override
        public int run(final CommandContext<CommandSender> context) {
            final String[] input = context.getInput().split(" ");
            final String[] arguments = new String[input.length - 1];
            System.arraycopy(input, 1, arguments, 0, arguments.length);
            executable.execute(context.getSource(), input[0], arguments);
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        }

        @Override
        public boolean test(final CommandSender sender) {
            if (permission == null) return true;
            return sender.hasPermission(permission);
        }
    }
}
