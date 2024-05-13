package io.github.disbatch.command.parameter;

import io.github.disbatch.command.CommandInput;

class InvalidInputImpl implements InvalidInput {
    private final CommandInput original;
    private final Reason reason;

    InvalidInputImpl(final CommandInput original, final Reason reason) {
        this.original = original;
        this.reason = reason;
    }

    @Override
    public int getArgumentLength() {
        return original.getArgumentLength();
    }

    @Override
    public String getArgumentLine() {
        return original.getArgumentLine();
    }

    @Override
    public String getArgument(final int index) {
        return original.getArgument(index);
    }

    @Override
    public String[] getArguments() {
        return original.getArguments();
    }

    @Override
    public String getCommandLabel() {
        return original.getCommandLabel();
    }

    @Override
    public String getCommandLine() {
        return original.getCommandLine();
    }

    @Override
    public Reason getReason() {
        return reason;
    }
}
