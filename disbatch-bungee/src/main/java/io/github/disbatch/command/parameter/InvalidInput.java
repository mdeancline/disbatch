package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;

/**
 * Represents a type of {@link CommandInput} that is unable to be processed by a {@link ParameterizedCommand}.
 *
 * @since 1.0.0
 */
public interface InvalidInput extends CommandInput {

    /**
     * Retrieves the reason for the {@code InvalidInput}.
     *
     * @return the reason
     */
    Reason getReason();

    /**
     * Represents various situations for the creation of an {@link InvalidInput}.
     */
    enum Reason {

        /**
         * Describes an {@link InvalidInput} as having the necessary arguments but still cannot be parsed as a whole
         * by the {@link ParameterizedCommand} that attempted to do so and failed.
         */
        INSUFFICIENT_ARGUMENTS,
        
        /**
         * Describes an {@link InvalidInput} as lacking the necessary arguments to be fully parsed.
         */
        LACKING_ARGUMENTS,

        /**
         * Describes an {@link InvalidInput} as having arguments that expand beyond the bounds of the originating
         * {@link ParameterizedCommand}'s line parameters.
         */
        EXTRA_ARGUMENTS
    }
}
