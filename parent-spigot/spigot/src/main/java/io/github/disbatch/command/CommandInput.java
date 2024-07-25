package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;

import java.io.Serializable;

/**
 * Holds various components of the command line used to execute a specific command.
 *
 * @apiNote The {@link Serializable} interface has been implemented since 1.1.0.
 * @since 1.0.0
 */
public interface CommandInput extends Iterable<CommandInputBinding>, Serializable {

    /**
     * Retrieves the number of passed arguments.
     *
     * @return the number of arguments.
     * @deprecated use the {@code length} property from {@link CommandInput#getArguments()} instead.
     */
    @Deprecated
    default int getArgumentLength() {
        return getArguments().length;
    }

    /**
     * Retrieves the passed arguments as a single string.
     *
     * @return the argument line as a single string.
     */
    String getArgumentLine();

    /**
     * Retrieves an argument from the specified index.
     *
     * @param index the index of the argument to retrieve.
     * @return the argument at the specified index.
     * @throws ArgumentIndexOutOfBoundsException if the specified index is out of bounds.
     */
    String getArgument(int index);

    /**
     * Retrieves all passed arguments.
     *
     * @return an array of all passed arguments, split by whitespace.
     */
    String[] getArguments();

    /**
     * Retrieves the command label associated with the input.
     *
     * @return the command label.
     */
    String getCommandLabel();

    /**
     * Retrieves the full command line used to execute a specific command.
     *
     * @return the full command line.
     */
    String getCommandLine();
}
