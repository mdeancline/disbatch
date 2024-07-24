package io.github.disbatch.command;

import io.github.disbatch.Command;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.SimpleLiteral;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//TODO complete development

/**
 * Introduces the concept of executing various commands belonging to a root command.
 *
 * @param <S> {@inheritDoc}
 * @since 1.0.0
 */
public final class CommandGroup implements CommandSyntaxExecutor<CommandSender, CommandInput> {
    private final Map<String, Command.Executable> executables = new HashMap<>();
    private final Map<String, CommandSyntax<?, ?>> syntaxes = new HashMap<>();
    private final GroupedCommandSyntax syntax;

    public CommandGroup(@NotNull final String label) {
        syntax = new GroupedCommandSyntax(label);
    }

    /**
     * Adds a command to be linked to this one.
     *
     * @param command
     */
    public CommandGroup with(@NotNull final Command command) {
        final String label = command.getLabel();

        final Command.Executable executable = command.getExecutable();
        final CommandSyntax<?, ?> syntax = command.getSyntax();
        executables.put(label, executable);
        syntaxes.put(label, syntax);
        this.syntax.addChild(syntax);

        for (final String alias : command.getAliases()) {
            executables.put(alias, executable);
            syntaxes.put(alias, syntax);
        }

        return this;
    }

    @Override
    public void execute(final CommandSender sender, final CommandInput input) {
        final String[] arguments = input.getArguments();
        final String label = arguments[0];
        final String[] newArguments = new String[arguments.length - 1];
        System.arraycopy(arguments, 1, newArguments, 0, newArguments.length);
        executables.get(input.getArgument(0)).execute(sender, label, newArguments);
    }

    @Override
    public CommandSyntax<CommandSender, CommandInput> getSyntax() {
        return syntax;
    }

    private final class GroupedCommandSyntax implements CommandSyntax<CommandSender, CommandInput> {
        private final SimpleLiteral literal;

        private GroupedCommandSyntax(final String label) {
            literal = new SimpleLiteral(label, false);
        }

        @Override
        public @Nullable CommandInput parse(final CommandSender sender, final CommandInput input) {
            return input;
        }

        @Override
        public Collection<Suggestion> getSuggestions(final CommandSender sender, final String[] arguments) {
            return Suggestion.ofTexts(executables.keySet());
        }

        @Override
        @NotNull
        public Literal getLiteral(final int index) {
            return index > 0 ? syntax.getLiteral(index - 1) : literal;
        }

        @Override
        public boolean matches(final CommandInput.Binding binding) {
            return executables.containsKey(binding.getArgument());
        }

        @Override
        public int getMinimumUsage() {
            return 1;
        }

        @Override
        public int getMaximumUsage() {
            return 1;
        }

        @NotNull
        @Override
        public Iterator<Literal> iterator() {
            return null;
        }

        public void addChild(final CommandSyntax<?,?> syntax) {
            literal.addChild(syntax.getLiteral(0));
        }
    }
}
