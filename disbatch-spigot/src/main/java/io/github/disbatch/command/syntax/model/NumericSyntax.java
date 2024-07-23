package io.github.disbatch.command.syntax.model;

import io.github.disbatch.command.syntax.AbstractSyntax;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code CommandSyntax} abstraction holding the functionalities necessary to create or retrieve an {@code Object}
 * relating to numeric use-cases based on parsable, passed arguments.
 *
 * @param <S> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @since 1.1.0
 */
public abstract class NumericSyntax<S extends CommandSender, V> extends AbstractSyntax<S, V> {
    private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String DECIMAL = ".";

    protected NumericSyntax(@NotNull final String... labels) {
        super(labels);
    }

    /**
     * Determines if the given argument can be parsed to a number (e.g a
     * {@code double}, {@code float}, or {@code int}).
     *
     * @param argument the argument passed from a command.
     * @return if the passed arguments can be parsed.
     */
    protected final boolean isNumber(@NotNull final String argument) {
        return isFloating(argument) || isInteger(argument);
    }

    /**
     * Determines if the given argument can be parsed to a floating-point number (e.g a
     * {@code double} or {@code float}).
     *
     * @param argument the argument passed from a command.
     * @return if the passed arguments can be parsed.
     */
    protected final boolean isFloating(@NotNull final String argument) {
        return argument.contains(DECIMAL) && argument.matches(NUMBER_REGEX);
    }

    /**
     * Determines if the given argument can be parsed to an integer.
     *
     * @param argument the argument passed from a command.
     * @return if the passed arguments can be parsed.
     */
    protected final boolean isInteger(@NotNull final String argument) {
        return !argument.contains(DECIMAL) && argument.matches(NUMBER_REGEX);
    }

    /**
     * Parses the given argument to an {@code int}.
     *
     * @param argument the given argument.
     * @return the integer value represented by the argument.
     * @throws NumberFormatException if the argument does not contain a parsable integer.
     */
    protected final int parseInt(@NotNull final String argument) {
        return Integer.parseInt(argument);
    }

    /**
     * Parses the given argument to a {@code double}.
     *
     * @param argument the given argument.
     * @return the double value represented by the argument.
     * @throws NumberFormatException if the argument does not contain a parsable double.
     */
    protected final double parseDouble(@NotNull final String argument) {
        return Double.parseDouble(argument.contains(DECIMAL) ? argument : argument + DECIMAL);
    }

    /**
     * Parses the given argument to a {@code float}.
     *
     * @param argument the given argument.
     * @return the double value represented by the argument.
     * @throws NumberFormatException if the argument does not contain a parsable float.
     */
    protected final float parseFloat(@NotNull final String argument) {
        return Float.parseFloat(argument.contains(DECIMAL) ? argument : argument + DECIMAL);
    }
}
