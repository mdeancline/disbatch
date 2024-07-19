package io.github.disbatch.command;

/**
 * Represents a type of {@link CommandInput} that is unable to be processed by a command.
 *
 * @since 1.1.0
 */
public interface CommandFailure extends CommandInput {

    /**
     * Retrieves the reason for the failure.
     *
     * @return the reason
     */
    Reason getReason();

    /**
     * Represents various situations for why a command failed to execute.
     */
    enum Reason {
        INVALID_SENDER,

        /**
         * Describes a command having the necessary arguments but still cannot be parsed as a whole by it and failed.
         */
        INSUFFICIENT_ARGUMENTS,

        /**
         * Describes a command lacking the necessary arguments to be executed with a fully parsed argument.
         */
        LACKING_ARGUMENTS,

        /**
         * Describes a command execution as having arguments that expand beyond the bounds of the originating
         * command.
         */
        EXTRA_ARGUMENTS
    }
}