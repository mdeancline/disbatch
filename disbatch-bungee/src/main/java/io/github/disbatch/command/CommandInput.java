package io.github.disbatch.command;

import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;

/**
 * Holds various components of the command line used to execute a specific {@link Command}.
 *
 * @since 1.0.0
 */
public interface CommandInput {

    /**
     * Retrieves the amount of passed arguments.
     *
     * @return the argument amount.
     */
    int getArgumentLength();

    /**
     * Retrieves the passed arguments as a single {@code String}.
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
}
