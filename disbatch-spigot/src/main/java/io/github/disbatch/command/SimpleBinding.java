package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandSyntax;

/**
 * Represents a binding of command arguments to a specific {@link CommandSyntax.Literal}, providing access to the
 * literal's label, the associated arguments, and the index of the current argument.
 *
 * @since 1.1.0
 */
public final class SimpleBinding implements CommandInput.Binding {
    private final CommandSyntax.Literal literal;
    private final String[] arguments;
    private final int index;
    private String argumentLine;

    /**
     * Constructs a new {@code SimpleBinding} with the specified literal, arguments, and index.
     *
     * @param literal   the {@link CommandSyntax.Literal} that this binding is associated with
     * @param arguments the array of arguments for the command
     * @param index     the index of the argument within the arguments array
     */
    public SimpleBinding(final CommandSyntax.Literal literal, final String[] arguments, final int index) {
        this.literal = literal;
        this.arguments = arguments;
        this.index = index;
    }

    @Override
    public String getLabel() {
        return literal.getLabel();
    }

    @Override
    public String getArgument() {
        return arguments[index];
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String getArgumentLine() {
        if (argumentLine == null)
            return argumentLine = String.join(" ", arguments);

        return argumentLine;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
