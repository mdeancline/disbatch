package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandLiteral;

/**
 * Represents a link between the arguments from a {@link CommandInput}, the label, and the relative execution
 * index of an executed command.
 *
 * @since 1.1.0
 */
public interface CommandInputBinding extends CommandLiteral {

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
