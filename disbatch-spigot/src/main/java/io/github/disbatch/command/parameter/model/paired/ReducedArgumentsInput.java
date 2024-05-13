package io.github.disbatch.command.parameter.model.paired;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;
import io.github.disbatch.command.parameter.model.Parameter;

class ReducedArgumentsInput implements CommandInput {
    private final Parameter<?, ?> first;
    private final CommandInput original;
    private String[] arguments;

    ReducedArgumentsInput(final Parameter<?, ?> first, final CommandInput original) {
        this.first = first;
        this.original = original;
    }

    @Override
    public int getArgumentLength() {
        return original.getArgumentLength() - first.getMaximumUsage();
    }

    @Override
    public String getArgumentLine() {
        return original.getArgumentLine();
    }

    @Override
    public String getArgument(final int index) {
        if (index < 0 || index >= getArgumentLength())
            throw new ArgumentIndexOutOfBoundsException(index);

        return getArguments()[index];
    }

    @Override
    public String[] getArguments() {
        if (arguments == null) {
            final String[] arguments = (this.arguments = new String[0]);
            System.arraycopy(original.getArguments(), first.getMaximumUsage() - 1, arguments, 0, getArgumentLength());
        }

        return arguments;
    }

    @Override
    public String getCommandLabel() {
        return original.getCommandLabel();
    }

    @Override
    public String getCommandLine() {
        return original.getCommandLine();
    }
}
