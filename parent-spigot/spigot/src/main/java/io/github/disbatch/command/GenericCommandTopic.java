package io.github.disbatch.command;

import io.github.disbatch.Command;
import io.github.disbatch.command.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO make valid command usage mechanism possible

/**
 * Serves as a default implementation that will be instantiated and registered into the server's {@link HelpMap}
 * when no alternative is provided for a {@link Command}.
 *
 * You can extend this class to create a custom {@code CommandTopic}, or use it as a reference for implementing your own.
 *
 * @implSpec Internally, this class uses an instance of {@link GenericCommandHelpTopic} to retrieve all required formatted help contents.
 * @since 1.1.0
 */
public class GenericCommandTopic<S extends CommandSender> implements CommandTopic<S> {
    private final String description;
    //    private final CommandSyntaxMessage usage;
    private GenericCommandHelpTopic bukkitTopic;

//    public GenericCommandTopic(@NotNull final String description) {
//        this(description, new ParameterUsage.Builder().labelHead('<').labelTail('>').build());
//    }

    /**
     * @param description
     */
    public GenericCommandTopic(@NotNull final String description) {
        this.description = description;
//        this.usage = usage;
    }

    @Override
    public void amend(final @Nullable String shortText, final @Nullable String fullText) {
        throwIfNotApplied();
        bukkitTopic.amendTopic(shortText, fullText);
    }

    @Override
    public void apply(final Command command) {
        bukkitTopic = new GenericCommandHelpTopic(new PlaceholderCommand(command, description));
    }

    @Override
    public boolean canSee(final CommandSender sender) {
        return true;
    }

    @Override
    public String getShortText() {
        throwIfNotApplied();
        return bukkitTopic.getShortText();
    }

    @Override
    public String getFullText(final CommandSender forSender) {
        throwIfNotApplied();
        return bukkitTopic.getFullText(forSender);
    }

    private void throwIfNotApplied() {
        if (bukkitTopic == null)
            throw new CommandException("GenericCommandTopic isn't fully initialized");
    }

    private static class PlaceholderCommand extends org.bukkit.command.Command {
        private PlaceholderCommand(final Command command, final String description) {
            super(
                command.getLabel(),
                description,
                "/<command>",
                command.getAliases()
            );
        }

        @Override
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
            return false;
        }
    }
}
