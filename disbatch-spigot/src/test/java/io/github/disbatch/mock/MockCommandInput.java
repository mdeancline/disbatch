package io.github.disbatch.mock;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import io.github.disbatch.command.CommandInput;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class MockCommandInput implements CommandInput {
    private final String label;
    private final String cmdLine;
    private final String argumentLine;
    private final String[] arguments;

    public MockCommandInput(final String cmdLine) {
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

    @NotNull
    @Override
    public Iterator<Binding> iterator() {
        return Iterators.emptyIterator();
    }
}
