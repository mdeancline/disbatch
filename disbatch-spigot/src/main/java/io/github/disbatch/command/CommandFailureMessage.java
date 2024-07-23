package io.github.disbatch.command;

/**
 * Defines a way of generating a string representation of a command failure message based on a given failure reason.
 *
 * @since 1.1.0
 */
@FunctionalInterface
public interface CommandFailureMessage {

    /**
     * Generates a string representation of the command failure message based on the specified reason.
     *
     * @param reason the reason for the command failure
     * @return the string representation of the command failure message
     */
    String toString(CommandFailure.Reason reason);
}
