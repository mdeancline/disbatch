package io.github.disbatch;

import io.github.disbatch.command.CommandInput;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

class SingleLabelCommandInput implements CommandInput {
    private final String label;

    SingleLabelCommandInput(final String label) {
        this.label = label;
    }

    @Override
    public int getArgumentLength() {
        return 0;
    }

    @Override
    public String getArgumentLine() {
        return emptyString();
    }

    @Override
    public String getArgument(final int index) {
        return emptyString();
    }

    private String emptyString() {
        return StringUtils.EMPTY;
    }

    @Override
    public String[] getArguments() {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    @Override
    public String getCommandLabel() {
        return label;
    }

    @Override
    public String getCommandLine() {
        return label;
    }
}
