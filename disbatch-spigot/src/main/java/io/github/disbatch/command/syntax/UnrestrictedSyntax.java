package io.github.disbatch.command.syntax;

import io.github.disbatch.command.CommandInput;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public final class UnrestrictedSyntax<S extends CommandSender> extends AbstractSyntax<S, CommandInput> {
    private static final UnrestrictedSyntax<CommandSender> INSTANCE = new UnrestrictedSyntax<>();

    private UnrestrictedSyntax() {
        super(StringUtils.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public static <S extends CommandSender> UnrestrictedSyntax<S> getInstance() {
        return (UnrestrictedSyntax<S>) INSTANCE;
    }

    @Override
    protected boolean isGreedy() {
        return true;
    }

    @Nullable
    @Override
    public CommandInput parse(final CommandSender sender, final CommandInput input) {
        return input;
    }

    @Override
    public boolean matches(final CommandInput.Binding binding) {
        return true;
    }

    @Override
    public int getMinimumUsage() {
        return 0;
    }

    @Override
    public int getMaximumUsage() {
        return Integer.MAX_VALUE;
    }
}
