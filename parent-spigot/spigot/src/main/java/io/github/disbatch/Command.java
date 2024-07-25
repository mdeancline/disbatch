package io.github.disbatch;

import com.google.common.collect.Lists;
import io.github.disbatch.command.*;
import io.github.disbatch.command.exception.CommandCreationException;
import io.github.disbatch.command.exception.CommandExecutionException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import io.github.disbatch.command.syntax.CommandLiteral;
import io.github.disbatch.command.syntax.CommandSyntax;
import io.github.disbatch.command.syntax.CommandSyntaxExecutor;
import io.github.disbatch.command.syntax.Suggestion;
import io.github.disbatch.command.syntax.exception.CommandSyntaxBoundsException;
import io.github.disbatch.command.syntax.exception.CommandSyntaxParseException;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a registered command with associated metadata including its label, execution logic, syntax, and failure
 * handling.
 *
 * @apiNote A {@code Command} can only be created using any of the following static builder methods:
 * <ul>
 *     <li>{@link Command#builder(CommandSyntaxExecutor)}</li>
 *     <li>{@link Command#builder(Class, CommandSyntaxExecutor)}</li>
 *     <li>{@link Command#builder(CommandExecutor)}</li>
 *     <li>{@link Command#builder(Class, CommandExecutor)}</li>
 * </ul>
 * @since 1.1.0
 */
public final class Command {
    private final Executable executable;
    private final CommandExecutor<CommandSender, Object> executor;
    private final CommandFailureHandler handler;
    private final String label;
    private final List<String> aliases;
    private final Class<? extends CommandSender> senderType;
    private final CommandSyntax<CommandSender, Object> syntax;
    private final CommandTopic<CommandSender> topic;
    private final Permission permission;

    @SuppressWarnings("unchecked")
    private Command(
            @NotNull final CommandExecutor<?, ?> executor,
            @NotNull final CommandFailureHandler handler,
            @NotNull final List<String> aliases,
            @NotNull final Class<? extends CommandSender> senderType,
            @NotNull final CommandSyntax<?, ?> syntax,
            @NotNull final CommandTopic<?> topic,
            @NotNull final String label,
            @NotNull final Permission permission
    ) {
        executable = new Executable();

        this.executor = (CommandExecutor<CommandSender, Object>) executor;
        this.handler = handler;
        this.aliases = aliases;
        this.senderType = senderType;
        this.syntax = (CommandSyntax<CommandSender, Object>) syntax;
        this.topic = (CommandTopic<CommandSender>) topic;
        this.label = label;
        this.permission = permission;
    }

    /**
     * Creates a builder for constructing a {@link Command} with default settings.
     *
     * @param executor the command executor for this command
     * @param <V>      the type of command arguments
     * @return a new {@code BasicBuilder} instance
     */
    public static <V> BasicBuilder<CommandSender, V> builder(@NotNull final CommandSyntaxExecutor<CommandSender, V> executor) {
        return builder(CommandSender.class, executor);
    }

    /**
     * Creates a builder for constructing a {@link Command} with the specified sender type.
     *
     * @param senderType the type of command sender
     * @param executor   the command executor for this command
     * @param <S>        the type of command sender
     * @param <V>        the type of command arguments
     * @return a new {@code BasicBuilder} instance
     */
    public static <S extends CommandSender, V> BasicBuilder<S, V> builder(@NotNull final Class<S> senderType, @NotNull final CommandSyntaxExecutor<S, V> executor) {
        return new Builder<>(senderType, executor).syntax(executor.getSyntax());
    }

    /**
     * Creates a builder for constructing a {@link Command} with default settings.
     *
     * @param executor the command executor for this command
     * @param <V>      the type of command arguments
     * @return a new {@code AdvancedBuilder} instance
     */
    public static <V> AdvancedBuilder<CommandSender, V> builder(@NotNull final CommandExecutor<CommandSender, V> executor) {
        return builder(CommandSender.class, executor);
    }

    /**
     * Creates a builder for constructing a {@link Command} with the specified sender type.
     *
     * @param senderType the type of command sender
     * @param executor   the command executor for this command
     * @param <S>        the type of command sender
     * @param <V>        the type of command arguments
     * @return a new {@code AdvancedBuilder} instance
     */
    public static <S extends CommandSender, V> AdvancedBuilder<S, V> builder(@NotNull final Class<S> senderType, @NotNull final CommandExecutor<S, V> executor) {
        return new Builder<>(senderType, executor);
    }

    /**
     * Gets the {@link Executable} associated with this registration.
     *
     * @return the command
     */
    public Executable getExecutable() {
        return executable;
    }

    /**
     * Gets the command label.
     *
     * @return the command label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the {@link CommandTopic} associated with this command.
     *
     * @return the command topic
     */
    public CommandTopic<CommandSender> getTopic() {
        return topic;
    }

    /**
     * Gets the type of command sender for which this command is intended.
     *
     * @return the sender type
     */
    public Class<? extends CommandSender> getSenderType() {
        return senderType;
    }

    /**
     * Gets the {@link CommandSyntax} used for parsing command arguments.
     *
     * @return the command syntax
     */
    public CommandSyntax<CommandSender, ?> getSyntax() {
        return syntax;
    }

    /**
     * Gets the aliases for this command.
     *
     * @return a collection of aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    @Nullable
    public Permission getPermission() {
        return permission;
    }

    /**
     * Retrieves a specific literal by its index.
     *
     * @param index the index of the literal
     * @return the literal at the specified index, or {@code null} if it doesn't exist
     */
    @Nullable
    public CommandLiteral getLiteral(final int index) {
        return syntax.getLiteral(index);
    }

    /**
     * Represents a basic {@link Command} builder, allowing for setting optional parameters.
     *
     * @param <S> the type of command sender
     * @param <V> the type of command arguments
     */
    public interface BasicBuilder<S extends CommandSender, V> {

        BasicBuilder<S, V> onFailure(@NotNull CommandFailureHandler handler);

        /**
         * Sets the {@link CommandTopic} for this command, which will be added to the server's {@link HelpMap}.
         *
         * @param topic the command topic
         * @return this builder
         */
        BasicBuilder<S, V> topic(@NotNull CommandTopic<S> topic);

        /**
         * Sets the command label aliases.
         *
         * @param aliases the aliases
         * @return this builder
         */
        BasicBuilder<S, V> aliases(@NotNull String... aliases);

        /**
         * Sets the command label.
         *
         * @param label the command label
         * @return this builder
         */
        AdvancedBuilder<S, V> label(@NotNull String label);

        AdvancedBuilder<S, V> permission(@NotNull String name);

        AdvancedBuilder<S, V> permission(@NotNull Permission permission);

        /**
         * Creates a new {@link Command}.
         *
         * @return the created registration
         */
        @NotNull
        Command build();
    }

    /**
     * Represents an advanced {@link Command} builder, allowing for setting all possible parameters.
     *
     * @param <S> the type of command sender
     * @param <V> the type of command arguments
     */
    public interface AdvancedBuilder<S extends CommandSender, V> {

        AdvancedBuilder<S, V> syntax(@NotNull CommandSyntax<? super S, V> syntax);

        AdvancedBuilder<S, V> onFailure(@NotNull CommandFailureHandler handler);

        /**
         * Sets the {@link CommandTopic} for this command, which will be added to the server's {@link HelpMap}.
         *
         * @param topic the command topic
         * @return this builder
         */
        AdvancedBuilder<S, V> topic(@NotNull CommandTopic<S> topic);

        /**
         * Sets the command label aliases.
         *
         * @param aliases the aliases
         * @return this builder
         */
        AdvancedBuilder<S, V> aliases(@NotNull String... aliases);

        /**
         * Sets the command label.
         *
         * @param label the command label
         * @return this builder
         */
        AdvancedBuilder<S, V> label(@NotNull String label);

        AdvancedBuilder<S, V> permission(@NotNull String name);

        AdvancedBuilder<S, V> permission(@NotNull Permission permission);

        /**
         * Creates a new {@link Command}.
         *
         * @return the created registration
         */
        @NotNull
        Command build();
    }

    /**
     * Builder for creating a {@link Command}.
     *
     * @param <S> the type of command sender
     * @param <V> the type of command arguments
     */
    public static final class Builder<S extends CommandSender, V> implements BasicBuilder<S, V>, AdvancedBuilder<S, V> {
        private static final CommandFailureHandler DEFAULT_FAILURE_HANDLER = (sender, failure) -> {
        };

        private final Class<S> senderType;
        private final CommandExecutor<S, V> executor;
        private Permission permission;
        private CommandSyntax<? super S, V> syntax;
        private CommandFailureHandler handler = DEFAULT_FAILURE_HANDLER;
        private CommandTopic<S> topic = new GenericCommandTopic<>("A plugin provided command.");
        private String label;
        private List<String> aliases = Collections.emptyList();

        private Builder(@NotNull final Class<S> senderType, @NotNull final CommandExecutor<S, V> executor) {
            this.senderType = senderType;
            this.executor = executor;
        }

        @Override
        public Builder<S, V> syntax(@NotNull final CommandSyntax<? super S, V> syntax) {
            final int minUsage = syntax.getMinimumUsage();
            final int maxUsage = syntax.getMaximumUsage();

            if (minUsage < 0)
                throw new CommandSyntaxBoundsException("Minimum usage cannot be less than 0");
            if (maxUsage <= 0)
                throw new CommandSyntaxBoundsException("Maximum usage must be greater than 0");
            if (minUsage > maxUsage)
                throw new CommandSyntaxBoundsException("Minimum usage cannot exceed maximum usage");

            this.syntax = syntax;
            return this;
        }

        @Override
        public Builder<S, V> onFailure(@NotNull final CommandFailureHandler handler) {
            this.handler = handler;
            return this;
        }

        @Override
        public Builder<S, V> topic(@NotNull final CommandTopic<S> topic) {
            this.topic = topic;
            return this;
        }

        @Override
        public Builder<S, V> label(@NotNull final String label) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Label \"" + label + "\" is registered as an alias");

            this.label = label;
            return this;
        }

        @Override
        public Builder<S, V> permission(@NotNull final String name) {
            return permission(new Permission(name));
        }

        @Override
        public Builder<S, V> permission(@NotNull final Permission permission) {
            this.permission = permission;
            return this;
        }

        @Override
        public Builder<S, V> aliases(@NotNull final String... aliases) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Alias \"" + alias + "\" is registered as the label");

            this.aliases = Lists.newArrayList(aliases);
            return this;
        }

        public Builder<S, V> validSenderMessage(@NotNull final String validSenderMessage) {
            handler = new CommandFailureNotifier().with(CommandFailure.Reason.UNINTENDED_SENDER, validSenderMessage);
            return this;
        }

        @Override
        @NotNull
        public Command build() {
            if (label == null)
                throw new CommandCreationException("Command label is null");

            if (syntax == null)
                throw new CommandCreationException("CommandSyntax is null");

            return new Command(executor, handler, aliases, senderType, syntax, topic, label, permission);
        }
    }

    /**
     * Represents the command execution and syntax parsing logic for a command.
     *
     * @since 1.1.0
     */
    public final class Executable {
        private Executable() {
        }

        /**
         * Executes the command with the given sender and arguments.
         *
         * @param sender the command sender
         */
        public void execute(final CommandSender sender, final String label, final String[] arguments) {
            final CommandInput input = CommandInputs.of(label, arguments, syntax);

            if (permission != null && !sender.hasPermission(permission)) {
                handler.handle(sender, new CommandFailureImpl(input, CommandFailure.Reason.LACKING_PERMISSION));
                return;
            }

            if (senderType.isAssignableFrom(sender.getClass()))
                tryExecute(sender, input);
            else
                handler.handle(sender, new CommandFailureImpl(input, CommandFailure.Reason.UNINTENDED_SENDER));
        }

        private void tryExecute(final CommandSender sender, final CommandInput input) {
            try {
                executeSafe(sender, input);
            } catch (final Exception e) {
                throw new CommandExecutionException(e);
            }
        }

        private void executeSafe(final CommandSender sender, final CommandInput input) {
            final boolean hasLackingArgs = input.getArguments().length < syntax.getMinimumUsage();
            final boolean hasExtraArgs = input.getArguments().length > syntax.getMaximumUsage();

            if (!hasLackingArgs && !hasExtraArgs)
                parseThenExecute(sender, input);
            else
                handleInvalidArguments(sender, input, hasLackingArgs);
        }

        private void parseThenExecute(final CommandSender sender, final CommandInput input) {
            final Object result = tryParse(sender, input);

            if (result == null)
                handler.handle(sender, new CommandFailureImpl(input, CommandFailure.Reason.INSUFFICIENT_ARGUMENTS));
            else
                executor.execute(sender, result);
        }

        @Nullable
        private Object tryParse(final CommandSender sender, final CommandInput input) {
            try {
                return syntax.parse(sender, input);
            } catch (final Exception e) {
                throw new CommandSyntaxParseException(e);
            }
        }

        private void handleInvalidArguments(final CommandSender sender, final CommandInput input, final boolean hasLackingArgs) {
            final CommandFailure.Reason reason = hasLackingArgs
                    ? CommandFailure.Reason.LACKING_ARGUMENTS
                    : CommandFailure.Reason.EXTRA_ARGUMENTS;
            final CommandFailure failure = new CommandFailureImpl(input, reason);
            handler.handle(sender, failure);
        }

        /**
         * Gets suggestions for command arguments based on the current input.
         *
         * @param sender    the command sender
         * @param arguments the command arguments
         * @return a collection of suggestions
         */
        public Collection<Suggestion> getSuggestions(final CommandSender sender, final CommandInputBinding binding) {
            return senderType.isAssignableFrom(sender.getClass())
                    ? syntax.getSuggestions(sender, binding)
                    : Collections.emptyList();
        }

        private class CommandFailureImpl implements CommandFailure {
            private static final long serialVersionUID = 4993722379444726446L;

            private final CommandInput source;
            private final Reason reason;

            public CommandFailureImpl(final CommandInput source, final Reason reason) {
                this.source = source;
                this.reason = reason;
            }

            @Override
            public Reason getReason() {
                return reason;
            }

            @Override
            public String getArgumentLine() {
                return source.getArgumentLine();
            }

            @Override
            public String getArgument(final int index) {
                return source.getArgument(index);
            }

            @Override
            public String[] getArguments() {
                return source.getArguments();
            }

            @Override
            public String getCommandLabel() {
                return source.getCommandLabel();
            }

            @Override
            public String getCommandLine() {
                return source.getCommandLine();
            }

            @NotNull
            @Override
            public Iterator<CommandInputBinding> iterator() {
                return source.iterator();
            }
        }
    }
}
