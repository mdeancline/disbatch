package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandLiteral;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Represents a binding of command arguments to a specific {@link CommandLiteral}, providing access to its value, the
 * associated arguments, and the index of the current argument.
 *
 * @since 1.1.0
 */
public final class SimpleBinding implements CommandInputBinding {
    private final CommandLiteral literal;
    private final String[] arguments;
    private final int index;
    private String argumentLine;

    /**
     * Constructs a new {@code SimpleBinding} with the specified literal, arguments, and index.
     *
     * @param literal   the {@link CommandLiteral} that this binding is associated with
     * @param arguments the array of arguments for the command
     * @param index     the index of the argument within the arguments array
     */
    public SimpleBinding(@NotNull final CommandLiteral literal, @NotNull final String[] arguments, final int index) {
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

    @NotNull
    @Override
    public Iterator<CommandLiteral> iterator() {
        return literal.iterator();
    }
}
