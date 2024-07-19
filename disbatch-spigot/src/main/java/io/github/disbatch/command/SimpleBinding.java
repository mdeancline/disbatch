package io.github.disbatch.command;

import io.github.disbatch.command.syntax.CommandSyntax;

//TODO complete documentation
public final class SimpleBinding implements CommandInput.Binding {
    private final CommandSyntax.Literal literal;
    private final String[] arguments;
    private final int index;

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
    public int getArgumentLength() {
        return arguments.length;
    }

    //TODO return valid argument line
    @Override
    public String getArgumentLine() {
        return null;
    }

    //TODO return valid arguments
    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public int getIndex() {
        return index;
    }
}
