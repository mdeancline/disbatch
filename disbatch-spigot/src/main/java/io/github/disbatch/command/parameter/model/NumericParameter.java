package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.AbstractParameter;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import org.bukkit.command.CommandSender;

/**
 * A {@code Parameter} abstraction holding the functionalities necessary to create or retrieve an {@code Object}
 * relating to numeric use-cases based on parsable, passed arguments.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 *
 * @since 1.0.0
 */
public abstract class NumericParameter<S extends CommandSender, V> extends AbstractParameter<S, V> {
    private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String DECIMAL = ".";

    protected NumericParameter() {
    }

    /**
     * Determines if each argument of a given {@link CommandInput} can be parsed to a {@code double}.
     *
     * @param input the input passed from a {@link ParameterizedCommand}.
     * @return if the passed arguments can be parsed.
     */
    protected final boolean isDoubleParsable(final CommandInput input) {
        return isDoubleParsable(input, 0);
    }

    /**
     * Determines if each argument of a given {@link CommandInput} can be parsed to a {@code double}.
     *
     * @param input         the input passed from a {@link ParameterizedCommand}.
     * @param startingIndex the argument index to start the check.
     * @return if the passed arguments can be parsed.
     * @throws ArgumentIndexOutOfBoundsException if the {@code startingIndex} exceeds or is less than the argument length
     *                                           of the given {@link CommandInput}.
     */
    protected final boolean isDoubleParsable(final CommandInput input, final int startingIndex) {
        if (startingIndex >= input.getArgumentLength())
            throw new ArgumentIndexOutOfBoundsException(startingIndex);

        for (int i = startingIndex; i < input.getArgumentLength(); i++)
            if (!input.getArgument(i).matches(NUMBER_REGEX))
                return false;

        return true;
    }

    /**
     * Determines if each argument of a given {@link CommandInput} can be parsed to an {@code int}.
     *
     * @param input the input passed from a {@link ParameterizedCommand}.
     * @return if the passed arguments can be parsed.
     */
    protected final boolean isIntegerParsable(final CommandInput input) {
        return isIntegerParsable(input, 0);
    }

    /**
     * Determines if each argument of a given {@link CommandInput} can be parsed to an {@code int}.
     *
     * @param input         the input passed from a {@link ParameterizedCommand}.
     * @param startingIndex the argument index to start the check.
     * @return if the passed arguments can be parsed.
     * @throws ArgumentIndexOutOfBoundsException if the {@code startingIndex} exceeds or is less than the argument length
     *                                           of the given {@link CommandInput}.
     */
    protected final boolean isIntegerParsable(final CommandInput input, final int startingIndex) {
        if (startingIndex >= input.getArgumentLength())
            throw new ArgumentIndexOutOfBoundsException(startingIndex);

        for (int i = startingIndex; i < input.getArgumentLength(); i++) {
            final String argument = input.getArgument(i);

            if (argument.contains(DECIMAL) || !argument.matches(NUMBER_REGEX))
                return false;
        }

        return true;
    }

    /**
     * Parses the given argument to an {@code int}.
     *
     * @param arg the given argument.
     * @return the integer value represented by the argument.
     */
    protected final int parseInt(final String arg) {
        return Integer.parseInt(arg);
    }

    /**
     * Parses the given argument to a {@code double}.
     *
     * @param arg the given argument.
     * @return the double value represented by the argument.
     */
    protected final double parseDouble(final String arg) {
        return Double.parseDouble(arg.contains(DECIMAL) ? arg : arg + DECIMAL);
    }
}
