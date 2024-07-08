package io.github.disbatch.mock;

import io.github.disbatch.command.CommandInput;

import java.util.Arrays;

public class CommandLine implements CommandInput {
    private final String label;
    private final String cmdLine;
    private final String argumentLine;
    private final String[] arguments;

    public CommandLine(final String cmdLine) {
        this.cmdLine = cmdLine;

        final String[] split = cmdLine.split(" ");
        label = split[0];
        arguments = Arrays.copyOfRange(split, 1, split.length);
        argumentLine = String.join(" ", arguments);
    }

    @Override
    public int getArgumentLength() {
        return arguments.length;
    }

    @Override
    public String getArgumentLine() {
        return argumentLine;
    }

    @Override
    public String getArgument(final int index) {
        return arguments[index];
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String getCommandLabel() {
        return label;
    }

    @Override
    public String getCommandLine() {
        return cmdLine;
    }
}
