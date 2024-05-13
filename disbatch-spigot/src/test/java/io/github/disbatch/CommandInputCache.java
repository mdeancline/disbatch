package io.github.disbatch;

import io.github.disbatch.command.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class CommandInputCache {
    private final List<CommandInput> inputs = new ArrayList<>();

    public void addInput(final CommandInput input) {
        inputs.add(input);
    }
}
