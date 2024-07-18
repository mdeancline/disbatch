package io.github.disbatch.command.descriptor;

import io.github.disbatch.command.*;
import io.github.disbatch.command.exception.CommandExecutionException;
import io.github.disbatch.command.exception.CommandRegistrationException;
import io.github.disbatch.command.syntax.CommandSyntax;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holds various necessary and optional data that should be associated with a command, such as its label and
 * {@link CommandExecutor} (both required), and available aliases or {@link Suggester} (both optional).
 *
 * @apiNote A {@code CommandDescriptor} can only be created with either {@link CommandDescriptor#of(Class, io.github.disbatch.command.Command)}
 * or {@link CommandDescriptor#of(Class, CommandExecutor)}.
 * @see CommandDescriptor#of(io.github.disbatch.command.Command)
 * @see CommandDescriptor#of(CommandExecutor) 
 * @since 1.0.0
 */
public final class CommandDescriptor<S extends CommandSender, V> {
    private final List<String> aliases = new LinkedList<>();
    private final Class<S> senderType;
    private final Command command;
    private final CommandSyntax<S, V> syntax;
    private final CommandFailureHandler failureHandler;
    private final CommandTopic<? super S> topic;
    private final String label;

    private CommandDescriptor(
            final @NotNull String label,
            final @NotNull String[] aliases,
            final @NotNull Class<S> senderType,
            final @NotNull CommandExecutor<S, V> executor,
            final @NotNull CommandSyntax<S, V> syntax,
            final @NotNull CommandFailureHandler failureHandler,
            final @NotNull CommandTopic<? super S> topic
    ) {
        this.label = label;
        this.senderType = senderType;
        this.syntax = syntax;
        this.failureHandler = failureHandler;
        this.topic = topic;
        this.aliases.addAll(Arrays.asList(aliases));

        this.command = new Command(executor, syntax);
    }

    public static LimitedBuilder<CommandSender, String> of(final io.github.disbatch.command.Command<CommandSender> command) {
        return of(CommandSender.class, command);
    }

    public static <S extends CommandSender> LimitedBuilder<S, String> of(final Class<S> senderType, final io.github.disbatch.command.Command<S> command) {
        return new Builder<>(senderType, command).syntax(command.getSyntax());
    }

    public static <V> FullBuilder<CommandSender, V> of(final CommandExecutor<CommandSender, V> executor) {
        return of(CommandSender.class, executor);
    }
    
    public static <S extends CommandSender, V> FullBuilder<S, V> of(final Class<S> senderType, final CommandExecutor<S, V> executor) {
        return new Builder<>(senderType, executor);
    }

    public Command getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public CommandTopic<? super S> getTopic() {
        return topic;
    }

    public Class<S> getSenderType() {
        return senderType;
    }

    public CommandSyntax<S, V> getSyntax() {
        return syntax;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    /**
     * Serves as one of the flexible solutions for creating a new {@link CommandDescriptor}.
     *
     * @since 1.1.0
     */
    public interface LimitedBuilder<S extends CommandSender, V> {

        LimitedBuilder<S, V> onFailure(@NotNull CommandFailureHandler failureHandler);

        /**
         * Sets the {@link CommandTopic} that should be converted accordingly to be added to the server's {@link HelpMap}.
         *
         * @param topic the {@code CommandTopic} to be added to the server's {@code HelpMap}
         * @return the corresponding builder
         */
        LimitedBuilder<S, V> topic(@NotNull CommandTopic<S> topic);

        /**
         * Sets the command label for use by the created descriptor for when a {@link io.github.disbatch.command.Command} is registered with it.
         *
         * @param label the label that should be used for this builder
         * @return the corresponding builder
         */
        LimitedBuilder<S, V> label(@NotNull String label);

        /**
         * Sets the command label aliases, which is optional, for use by the created descriptor for when a {@link io.github.disbatch.command.Command}
         * is registered with it.
         *
         * @param aliases the aliases that should be used for this builder
         * @return the corresponding builder
         */
        LimitedBuilder<S, V> aliases(@NotNull String... aliases);

        /**
         * Sets the valid sender message to be displayed.
         *
         * @param validSenderMessage the message to be displayed for a valid sender
         * @return the corresponding builder
         */
        @Deprecated
        LimitedBuilder<S, V> validSenderMessage(@NotNull String validSenderMessage);

        /**
         * Creates a new {@link CommandDescriptor}.
         *
         * @return the created descriptor
         */
        @NotNull CommandDescriptor<S, V> build();
    }

    /**
     * Serves as one of the flexible solutions for creating a new {@link CommandDescriptor}.
     *
     * @since 1.1.0
     */
    public interface FullBuilder<S extends CommandSender, V> {

        FullBuilder<S, V> onFailure(@NotNull CommandFailureHandler failureHandler);

        /**
         * Sets the {@link CommandTopic} that should be converted accordingly to be added to the server's {@link HelpMap}.
         *
         * @param topic the {@code CommandTopic} to be added to the server's {@code HelpMap}
         * @return the corresponding builder
         */
        FullBuilder<S, V> topic(@NotNull CommandTopic<S> topic);

        /**
         * Sets the command label for use by the created descriptor for when a {@link io.github.disbatch.command.Command} is registered with it.
         *
         * @param label the label that should be used for this builder
         * @return the corresponding builder
         */
        FullBuilder<S, V> label(@NotNull String label);

        /**
         * Sets the command label aliases, which is optional, for use by the created descriptor for when a {@link io.github.disbatch.command.Command}
         * is registered with it.
         *
         * @param aliases the aliases that should be used for this builder
         * @return the corresponding builder
         */
        FullBuilder<S, V> aliases(@NotNull String... aliases);

        /**
         * Sets the valid sender message to be displayed.
         *
         * @param validSenderMessage the message to be displayed for a valid sender
         * @return the corresponding builder
         */
        @Deprecated
        FullBuilder<S, V> validSenderMessage(@NotNull String validSenderMessage);

        FullBuilder<S, V> syntax(@NotNull CommandSyntax<S, V> syntax);

        /**
         * Creates a new {@link CommandDescriptor}.
         *
         * @return the created descriptor
         */
        @NotNull CommandDescriptor<S, V> build();
    }

    private static final class Builder<S extends CommandSender, V> implements LimitedBuilder<S, V>, FullBuilder<S, V> {
        private static final CommandFailureHandler DEFAULT_FAILURE_HANDLER = (sender, failure) -> {};

        private final Class<S> senderType;
        private final CommandExecutor<S, V> executor;
        private CommandSyntax<S, V> syntax;
        private CommandFailureHandler failureHandler = DEFAULT_FAILURE_HANDLER;
        private CommandTopic<S> topic = new GenericCommandTopic<>("A plugin provided command.");
        private String label;
        private String[] aliases = ArrayUtils.EMPTY_STRING_ARRAY;

        private Builder(final @NotNull Class<S> senderType, final @NotNull CommandExecutor<S, V> executor) {
            this.senderType = senderType;
            this.executor = executor;
        }

        @Override
        public Builder<S, V> syntax(final @NotNull CommandSyntax<S, V> syntax) {
            this.syntax = syntax;
            return this;
        }

        @Override
        public Builder<S, V> onFailure(final @NotNull CommandFailureHandler failureHandler) {
            this.failureHandler = failureHandler;
            return this;
        }

        @Override
        public Builder<S, V> topic(final @NotNull CommandTopic<S> topic) {
            this.topic = topic;
            return this;
        }

        @Override
        public Builder<S, V> label(final @NotNull String label) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Label \"" + label + "\" is registered as an alias");

            this.label = label;
            return this;
        }

        @Override
        public Builder<S, V> aliases(final @NotNull String... aliases) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Alias \"" + alias + "\" is registered as the label");

            this.aliases = aliases;
            return this;
        }

        @Override
        public Builder<S, V> validSenderMessage(final @NotNull String validSenderMessage) {
            failureHandler = (sender, failure) -> {
                if (failure.getReason() == CommandFailure.Reason.INVALID_SENDER)
                    sender.sendMessage(validSenderMessage);
            };
            return this;
        }

        @Override
        public @NotNull CommandDescriptor<S, V> build() {
            if (label == null)
                throw new CommandRegistrationException("Command label cannot be empty");

            return new CommandDescriptor<>(
                    label,
                    aliases,
                    senderType,
                    executor,
                    syntax,
                    failureHandler,
                    topic
            );
        }
    }

    public final class Command {
        private final CommandExecutor<CommandSender, Object> executor;
        private final CommandSyntax<CommandSender, Object> syntax;

        @SuppressWarnings("unchecked")
        private Command(final CommandExecutor<S, ?> executor, final CommandSyntax<S, ?> syntax) {
            this.executor = (CommandExecutor<CommandSender, Object>) executor;
            this.syntax = (CommandSyntax<CommandSender, Object>) syntax;
        }

        public boolean execute(final CommandSender sender, final String commandLabel, final String[] arguments) {
            if (sender == null)
                throw new CommandExecutionException("CommandSender is null");

            final CommandInput input = CommandInputs.of(commandLabel, arguments, syntax);

            if (senderType.isAssignableFrom(sender.getClass()))
                execute(sender, input);
            else
                CommandDescriptor.this.failureHandler.handle(sender, new CommandFailure(syntax, input, CommandFailure.Reason.INVALID_SENDER));

            return true;
        }

        public void execute(final CommandSender sender, final CommandInput input) {
            final boolean hasLackingArgs = input.getArgumentLength() < syntax.getMinimumUsage();
            final boolean hasExtraArgs = input.getArgumentLength() > syntax.getMaximumUsage();

            if (!hasLackingArgs && !hasExtraArgs)
                parseThenExecute(sender, input);
            else
                handleInvalidArguments(sender, input, hasLackingArgs);
        }

        private void handleInvalidArguments(final CommandSender sender, final CommandInput input, boolean hasLackingArgs) {
            final CommandFailure.Reason reason = hasLackingArgs
                    ? CommandFailure.Reason.LACKING_ARGUMENTS
                    : CommandFailure.Reason.EXTRA_ARGUMENTS;
            final CommandFailure failure = new CommandFailure(syntax, input, reason);
            CommandDescriptor.this.failureHandler.handle(sender, failure);
        }

        private void parseThenExecute(final CommandSender sender, final CommandInput input) {
            final Object result = syntax.parse(sender, input);

            if (result == null)
                CommandDescriptor.this.failureHandler.handle(sender, new CommandFailure(syntax, input, CommandFailure.Reason.INSUFFICIENT_ARGUMENTS));
            else
                executor.execute(sender, input, result);
        }

        public Collection<Suggestion> getSuggestions(CommandSender sender, String[] args) {
            return senderType.isAssignableFrom(sender.getClass())
                    ? syntax.getSuggestions(sender, args)
                    : Collections.emptyList();
        }
    }
}