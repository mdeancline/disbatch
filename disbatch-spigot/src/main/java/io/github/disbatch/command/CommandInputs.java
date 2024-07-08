package io.github.disbatch.command;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A namespace for {@link CommandInput} convenience and utility methods.
 *
 * @since 1.0.0
 */
public final class CommandInputs {
    private static CommandInput EMPTY = new EmptyCommandInput();
    private CommandInputs() {
        throw new AssertionError();
    }

    /**
     * Retrieves an empty {@link CommandInput}.
     * @return the retrieved {@link CommandInput}.
     */
    public static CommandInput empty() {
        return EMPTY;
    }

    private static class EmptyCommandInput implements CommandInput {
        @Override
        public int getArgumentLength() {
            return 0;
        }

        @Override
        public String getArgumentLine() {
            return StringUtils.EMPTY;
        }

        @Override
        public String getArgument(int index) {
            if (index > 0)
                throw new IndexOutOfBoundsException();

            return StringUtils.EMPTY;
        }

        @Override
        public String[] getArguments() {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        @Override
        public String getCommandLabel() {
            return StringUtils.EMPTY;
        }

        @Override
        public String getCommandLine() {
            return StringUtils.EMPTY;
        }
    }
}
