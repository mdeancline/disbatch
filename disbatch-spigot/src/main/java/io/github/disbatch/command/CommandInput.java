package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;

import java.io.Serializable;

/**
 * Holds various components of the command line used to execute a specific command.
 *
 * @apiNote The {@link Serializable} interface has been implemented since 1.1.0.
 * @since 1.0.0
 */
public interface CommandInput extends Iterable<CommandInput.Binding>, Serializable {

    /**
     * Retrieves the amount of passed arguments.
     *
     * @return the argument amount.
     * @deprecated use the {@code length} property from {@link CommandInput#getArguments()} instead.
     */
    @Deprecated
    default int getArgumentLength() {
        return getArguments().length;
    }

    /**
     * Retrieves the passed arguments as a single string.
     *
     * @return the argument line.
     */
    String getArgumentLine();

    /**
     * Retrieves an argument from the specified index.
     *
     * @param index
     * @return
     * @throws ArgumentIndexOutOfBoundsException
     */
    String getArgument(int index);

    /**
     * Retrieves the passed arguments.
     *
     * @return all passed arguments, split via whitespace.
     */
    String[] getArguments();

    /**
     * Retrieves the command label used in association with the input.
     *
     * @return the command label.
     */
    String getCommandLabel();

    /**
     * Retrieves the command line used to execute a specific {@link Command}.
     *
     * @return the passed command line.
     */
    String getCommandLine();

    /**
     * Represents a link between its arguments, label, and relative execution index from an executed command.
     *
     * @since 1.1.0
     */
    interface Binding {

        /**
         * Retrieves the label of the command argument.
         *
         * @return the label of the command argument
         */
        String getLabel();

        /**
         * Retrieves the value of the command argument.
         *
         * @return the value of the command argument
         */
        String getArgument();

        /**
         * Retrieves the passed arguments associated with the binding.
         *
         * @return all associated arguments
         */
        String[] getArguments();

        /**
         * Retrieves the passed arguments as a single string.
         *
         * @return the argument line.
         */
        String getArgumentLine();

        /**
         * Retrieves the relative index in which it was executed with the command.
         *
         * @return the index of the command argument
         */
        int getIndex();
    }
}
