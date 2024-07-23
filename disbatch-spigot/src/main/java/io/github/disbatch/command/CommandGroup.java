package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.SimpleLiteral;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//TODO complete development

/**
 * Introduces the concept of executing various commands belonging to a root command.
 *
 * @param <S> {@inheritDoc}
 * @since 1.0.0
 */
public final class CommandGroup implements SyntaxExecutor<CommandSender, CommandInput> {
    private final Map<String, CommandRegistration.Command> commands = new HashMap<>();
    private final Map<String, CommandSyntax<?, ?>> syntaxes = new HashMap<>();
    private final GroupedCommandSyntax syntax;

    public CommandGroup(@NotNull final String label) {
        syntax = new GroupedCommandSyntax(label);
    }

    /**
     * Adds a command to be linked to this one through a {@link CommandRegistration}.
     *
     * @param registration
     */
    public CommandGroup with(@NotNull final CommandRegistration registration) {
        final String label = registration.getLabel();
        final CommandRegistration.Command command = registration.getCommand();
        commands.put(label, registration.getCommand());
        syntaxes.put(label, registration.getSyntax());

        for (final String alias : registration.getAliases()) {
            commands.put(alias, command);
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
        commands.get(input.getArgument(0)).execute(sender, label, newArguments);
    }

    @Override
    public CommandSyntax<CommandSender, CommandInput> getSyntax() {
        return syntax;
    }

    private static class GroupedCommandInput implements CommandInput {
        private static final long serialVersionUID = 2377622153887293994L;

        private final CommandInput previous;
        private final String cmdLabel;
        private String[] arguments;
        private String argumentLine;

        private GroupedCommandInput(final CommandInput previous) {
            this.previous = previous;
            cmdLabel = previous.getCommandLabel() + " " + previous.getArgument(0);
        }

        @Override
        public String getArgumentLine() {
            return argumentLine == null
                    ? (argumentLine = String.join(" ", getArguments()))
                    : argumentLine;
        }

        @Override
        public String getArgument(final int index) {
            final String[] arguments = getArguments();

            if (index < 0 || index >= arguments.length)
                throw new ArgumentIndexOutOfBoundsException(index);

            return arguments[index];
        }

        @Override
        public String[] getArguments() {
            if (arguments == null) {
                final String[] previousArguments = previous.getArguments();
                final String[] arguments = (this.arguments = new String[previousArguments.length - 1]);
                System.arraycopy(previousArguments, 1, arguments, 0, arguments.length);

                return arguments;
            }

            return arguments;
        }

        @Override
        public String getCommandLabel() {
            return cmdLabel;
        }

        @Override
        public String getCommandLine() {
            return previous.getCommandLine();
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

        @NotNull
        @Override
        public Iterator<Binding> iterator() {
            return null;
        }
    }

    private final class GroupedCommandSyntax implements CommandSyntax<CommandSender, CommandInput> {
        private final Literal literal;

        private GroupedCommandSyntax(final String label) {
            literal = new SimpleLiteral(label, false);
        }

        @Override
        public @Nullable CommandInput parse(final CommandSender sender, final CommandInput input) {
            return input;
        }

        @Override
        public Collection<Suggestion> getSuggestions(final CommandSender sender, final String[] arguments) {
            return Suggestion.ofTexts(commands.keySet());
        }

        @Override
        @NotNull
        public Literal getLiteral(final int index) {
            return literal;
        }

        @Override
        public boolean matches(final CommandInput.Binding binding) {
            return binding.getIndex() > 0
                    ? commands.containsKey(binding.getArgument())
                    : syntaxes.get(binding.getArguments()[0]).matches(new GroupedCommandInput());
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
    }
}
