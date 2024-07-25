package io.github.disbatch.command;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Represents a type of {@link CommandInput} that is unable to be processed by a command,
 * providing information on why a command failed and acts as the associated input.
 *
 * @since 1.1.0
 */
public interface CommandFailure extends CommandInput {

    /**
     * Retrieves the reason for the failure.
     *
     * @return the reason for the command failure
     */
    Reason getReason();

    /**
     * Represents various situations for why a command failed to execute.
     * Each reason describes a different kind of failure scenario.
     */
    enum Reason {

        /**
         * Indicates that the command was issued by a sender type that is not intended to execute this command.
         */
        UNINTENDED_SENDER(0),

        /**
         * Describes a command having the necessary arguments but still failing to be parsed as a whole.
         */
        INSUFFICIENT_ARGUMENTS(1),

        /**
         * Describes a command lacking the necessary arguments to be executed with a fully parsed argument.
         */
        LACKING_ARGUMENTS(2),

        /**
         * Describes a command execution where the arguments exceed the expected number or range for the command.
         */
        EXTRA_ARGUMENTS(3),

        /**
         * Describes the command sender not having permission to execute the command.
         */
        LACKING_PERMISSION(4);

        private final int id;

        Reason(final int id) {
            this.id = id;
        }

        /**
         * Provides a string representation of the failure reason.
         * The string is the lowercase name of the reason with underscores removed.
         *
         * @return the string representation of the reason
         */
        @Override
        public String toString() {
            return name().toLowerCase(Locale.getDefault()).replaceAll("_", "");
        }

        /**
         * Retrieves the {@code Reason} corresponding to the specified identifier.
         *
         * @param id the identifier of the failure reason
         * @return the corresponding {@code Reason}, or {@code null} if no matching reason is found
         */
        @Nullable
        public Reason fromId(final int id) {
            for (final Reason reason : values())
                if (reason.getId() == id)
                    return reason;

            return null;
        }

        /**
         * Retrieves the identifier of the failure reason.
         *
         * @return the identifier of the failure reason
         */
        public int getId() {
            return id;
        }
    }
}
