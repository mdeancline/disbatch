package io.github.disbatch.command.descriptor;

import io.github.disbatch.command.CommandInput;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @since 1.1.0
 */
public final class CommandFailure implements Iterable<CommandInput.Binding> {
    private final CommandInput input;
    private final Reason reason;

    CommandFailure(final CommandInput input, final Reason reason) {
        this.input = input;
        this.reason = reason;
    }

    @NotNull
    @Override
    public Iterator<CommandInput.Binding> iterator() {
        return input.iterator();
    }

    public Reason getReason() {
        return reason;
    }

    /**
     * Represents various situations for why a command failed to execute.
     */
    public enum Reason {
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
