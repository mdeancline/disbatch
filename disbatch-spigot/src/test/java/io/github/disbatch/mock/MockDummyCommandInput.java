package io.github.disbatch.mock;

public class MockDummyCommandInput extends MockCommandInput {
    public MockDummyCommandInput(final String arguments) {
        super("cmd " + arguments);
    }
}
