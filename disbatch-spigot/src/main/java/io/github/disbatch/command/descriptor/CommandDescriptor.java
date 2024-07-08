package io.github.disbatch.command.descriptor;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holds various necessary and optional data that should be associated with a {@link Command} upon registration, such as
 * its label (required) and available aliases (optional).
 *
 * @apiNote A {@code CommandDescriptor} can only be created with a {@link CommandDescriptor.Builder}.
 *
 * @since 1.0.0
 */
public final class CommandDescriptor {
    private final List<String> aliases = new LinkedList<>();
    private final CommandTopic topic;
    private final String label;
    private final String validSenderMessage;

    private CommandDescriptor(final @NotNull String label, final @NotNull String[] aliases, final @NotNull CommandTopic topic, final @NotNull String validSenderMessage) {
        this.label = label;
        this.topic = topic;
        this.validSenderMessage = validSenderMessage;
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public String getLabel() {
        return label;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getValidSenderMessage() {
        return validSenderMessage;
    }

    public CommandTopic getTopic() {
        return topic;
    }

    /**
     * Serves as the flexible solution for creating a new {@link CommandDescriptor}.
 *
 * @since 1.0.0
 */
    public static final class Builder {
        private static final Map<Class<? extends CommandTopic>, CommandTopicFinalizer<?>> FINALIZERS = new HashMap<>();
        private static final CommandTopicFinalizer<CommandTopic> DEFAULT_FINALIZER = new DefaultTopicFinalizer();

        private CommandTopic topic = new GenericCommandTopic("A plugin provided command.");
        private String label;
        private String[] aliases = ArrayUtils.EMPTY_STRING_ARRAY;
        private String validSenderMessage = StringUtils.EMPTY;

        static {
            FINALIZERS.put(GenericCommandTopic.class, new GenericCommandTopic.Finalizer());
        }

        private static Map<Class<? extends CommandTopic>, CommandTopicFinalizer<?>> getTopicFinalizers() {
            final Map<Class<? extends CommandTopic>, CommandTopicFinalizer<?>> topicFinalizers = new HashMap<>();

            topicFinalizers.put(GenericCommandTopic.class, new GenericCommandTopic.Finalizer());

            return topicFinalizers;
        }

        /**
         * Sets the {@link CommandTopic} that should be converted accordingly to be added to the server's {@link HelpMap}.
         *
         * @param topic the {@code CommandTopic} to be added to the server's {@code HelpMap}
         * @return the corresponding builder
         */
        public Builder topic(final @NotNull CommandTopic topic) {
            this.topic = topic;
            return this;
        }

        /**
         * Sets the command label for use by the created descriptor for when a {@link Command} is registered with it.
         *
         * @param label the label that should be used for this builder
         * @return the corresponding builder
         */
        public Builder label(final @NotNull String label) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Label \"" + label + "\" is registered as an alias");

            this.label = label;
            return this;
        }

        /**
         * Sets the command label aliases, which is optional, for use by the created descriptor for when a {@link Command}
         * is registered with it.
         *
         * @param aliases the aliases that should be used for this builder
         * @return the corresponding builder
         */
        public Builder aliases(final @NotNull String... aliases) {
            for (final String alias : aliases)
                if (alias.equals(label))
                    throw new CommandRegistrationException("Alias \"" + alias + "\" is registered as the label");

            this.aliases = aliases;
            return this;
        }

        /**
         * @param validSenderMessage
         * @return
         */
        public Builder validSenderMessage(final @NotNull String validSenderMessage) {
            this.validSenderMessage = validSenderMessage;
            return this;
        }

        /**
         * Creates a new {@link CommandDescriptor}.
         *
         * @return the created descriptor
         */
        @SuppressWarnings("unchecked")
        public @NotNull CommandDescriptor build() {
            if (label == null)
                throw new CommandRegistrationException("Command label cannot be empty");

            final MutableCommandTopic mutableTopic = new MutableCommandTopic(topic);
            final CommandDescriptor descriptor = new CommandDescriptor(label, aliases, mutableTopic, validSenderMessage);
            final CommandTopicFinalizer<?> finalizer = FINALIZERS.getOrDefault(topic.getClass(), DEFAULT_FINALIZER);

            final CommandTopicFinalizer<CommandTopic> topicFinalizer = (CommandTopicFinalizer<CommandTopic>) finalizer;

            mutableTopic.setTopic(topicFinalizer.finalize(topic, descriptor));

            return descriptor;
        }
    }

    private static class DefaultTopicFinalizer implements CommandTopicFinalizer<CommandTopic> {
        @Override
        public CommandTopic finalize(final CommandTopic topic, final CommandDescriptor descriptor) {
            return topic;
        }
    }

    private static class MutableCommandTopic implements CommandTopic {
        private CommandTopic innerTopic;

        MutableCommandTopic(final CommandTopic innerTopic) {
            this.innerTopic = innerTopic;
        }

        @Override
        public String applyAmendment(final String baseText, final String amendment) {
            return innerTopic.applyAmendment(baseText, amendment);
        }

        @Override
        public boolean canSee(final CommandSender forWho) {
            return innerTopic.canSee(forWho);
        }

        @Override
        public String getShortText() {
            return innerTopic.getShortText();
        }

        @Override
        public String getFullText(final CommandSender forWho) {
            return innerTopic.getFullText(forWho);
        }

        void setTopic(final CommandTopic innerTopic) {
            this.innerTopic = innerTopic;
        }
    }
}
