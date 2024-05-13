package io.github.disbatch.command.parameter.model.array;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.exception.ArgumentIndexOutOfBoundsException;

class SelectedArgumentsInput implements CommandInput {
    private final CommandInput original;
    private final String[] selectedArgs;
    private String argumentLine;
    private String commandLine;

    SelectedArgumentsInput(final CommandInput original, final String[] selectedArgs) {
        this.original = original;
        this.selectedArgs = selectedArgs;
    }

    @Override
    public int getArgumentLength() {
        return selectedArgs.length;
    }

    @Override
    public String getArgumentLine() {
        return argumentLine == null
                ? (argumentLine = String.join(" "))
                : argumentLine;
    }

    @Override
    public String getArgument(final int index) {
        if (index < 0 || index >= selectedArgs.length)
            throw new ArgumentIndexOutOfBoundsException(index);

        return selectedArgs[index];
    }

    @Override
    public String[] getArguments() {
        return selectedArgs;
    }

    @Override
    public String getCommandLabel() {
        return original.getCommandLabel();
    }

    @Override
    public String getCommandLine() {
        return commandLine == null
                ? (commandLine = original.getCommandLabel() + " " + getArgumentLine())
                : commandLine;
    }
}
