package io.github.disbatch.command.descriptor;

import io.github.disbatch.command.Command;
import io.github.disbatch.command.exception.CommandRegistrationException;
import org.jetbrains.annotations.NotNull;

/**
 * Holds various necessary and optional data that should be associated with a {@link Command} upon registration, such as
 * its label (required) and available aliases (optional).
 *
 * @apiNote A {@code CommandDescriptor} can only be created with a {@link Builder}.
 *
 * @since 1.0.0
 */
public final class CommandDescriptor {
    private final String[] aliases;
    private final String label;
    private final String validSenderMessage;

    private CommandDescriptor(final @NotNull String label, final @NotNull String[] aliases, final @NotNull String validSenderMessage) {
        this.label = label;
        this.validSenderMessage = validSenderMessage;
        this.aliases = aliases;
    }

    public String getLabel() {
        return label;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getValidSenderMessage() {
        return validSenderMessage;
    }

    /**
     * Serves as the flexible solution for creating a new {@link CommandDescriptor}.
 *
 * @since 1.0.0
 */
    public static final class Builder {
        private String label;
        private String[] aliases = new String[]{};
        private String validSenderMessage = "";

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
        public @NotNull CommandDescriptor build() {
            if (label == null)
                throw new CommandRegistrationException("Command label cannot be empty");

            return new CommandDescriptor(label, aliases, validSenderMessage);
        }
    }
}
