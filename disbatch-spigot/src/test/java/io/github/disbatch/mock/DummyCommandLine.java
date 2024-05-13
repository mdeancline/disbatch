package io.github.disbatch.mock;

public class DummyCommandLine extends CommandLine {
    public DummyCommandLine(final String arguments) {
        super("cmd " + arguments);
    }
}
