package io.github.disbatch.command.parameter;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Encapsulates various {@code String} and {@code char} components to create a usage message for any
 * {@link ParameterizedCommand}.
 * @see Builder
 *
 * @apiNote A {@code ParameterUsage} can only be created with a {@link ParameterUsage.Builder}.
 *
 * @since 1.0.0
 */
public final class ParameterUsage implements InvalidInputHandler<CommandSender> {
    private final String baseMessage;
    private final char labelHead;
    private final char labelTail;
    private final String[] usageLabels;

    private ParameterUsage(final @NotNull String baseMessage, final char labelHead, final char labelTail, final @NotNull String[] usageLabels) {
        this.baseMessage = baseMessage;
        this.labelHead = labelHead;
        this.labelTail = labelTail;
        this.usageLabels = usageLabels;
    }

    @Override
    public void handle(final CommandSender sender, final InvalidInput input) {
        final StringBuilder builder = new StringBuilder("/").append(input.getCommandLabel());

        for (final String label : usageLabels)
            builder.append(" ").append(labelHead).append(label).append(labelTail);

        sender.sendMessage(baseMessage.replace("%usage", builder));
    }

    /**
     * Serves as the flexible solution for creating a {@link ParameterUsage}.
     */
    public static final class Builder {
        private String baseMessage;
        private char labelHead;
        private char labelTail;
        private String[] usageLabels = new String[]{};

        /**
         * @param baseMessage the base message, optionally with the {@code %usage} placeholder
         *                    (e.g., {@code "Usage: %usage"}) for including the command line usage, to use upon creation.
         * @return the corresponding {@link Builder}
         */
        public Builder baseMessage(final @NotNull String baseMessage) {
            this.baseMessage = baseMessage;
            return this;
        }

        /**
         * @param labelHead the {@code char} to be prepended to a usage label
         * @return the corresponding {@link Builder}
         */
        public Builder labelHead(final char labelHead) {
            this.labelHead = labelHead;
            return this;
        }

        /**
         * @param labelTail the {@code char} to be appended to a usage label
         * @return the corresponding {@link Builder}
         */
        public Builder labelTail(final char labelTail) {
            this.labelTail = labelTail;
            return this;
        }

        /**
         * @param usageLabels the usage labels that apply to the {@link ParameterizedCommand} in question.
         * @return the corresponding {@link Builder}
         */
        public Builder usageLabels(final @NotNull String... usageLabels) {
            if (usageLabels.length > 0)
                this.usageLabels = usageLabels;

            return this;
        }

        /**
         * Creates a new {@link ParameterUsage}.
         *
         * @return the created {@link ParameterUsage}.
         */
        public ParameterUsage build() {
            return new ParameterUsage(baseMessage, labelHead, labelTail, usageLabels);
        }
    }
}
