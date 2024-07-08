package io.github.disbatch.command.parameter.model;

import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.TabCompleter;
import io.github.disbatch.command.TabCompleters;
import io.github.disbatch.command.parameter.Parameter;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;

/**
 * Parses a {@link Boolean} based on a parsable, passed argument.
 * <br>
 * <br>
 * <b>Argument Syntax:</b> [true|false]
 *
 * @since 1.0.0
 */
public final class BooleanParameter implements Parameter<CommandSender, Boolean> {
    private static final Collection<String> COMPLETIONS = Arrays.asList("true", "false");

    private final boolean tabComplete;

    public BooleanParameter() {
        this(false);
    }

    /**
     * @since 1.1.0
     */
    public BooleanParameter(final boolean tabComplete) {
        this.tabComplete = tabComplete;
    }
    
    @Override
    public int getMinimumUsage() {
        return 1;
    }

    @Override
    public int getMaximumUsage() {
        return 1;
    }

    @Override
    public Boolean parse(final CommandSender sender, final CommandInput input) {
        final String argument = input.getArgument(0);

        return argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("false")
                ? Boolean.valueOf(argument)
                : null;
    }

    @Override
    public Collection<String> tabComplete(final CommandSender sender, final CommandInput input) {
        return tabComplete ? COMPLETIONS : Parameter.super.tabComplete(sender, input);
    }
}
