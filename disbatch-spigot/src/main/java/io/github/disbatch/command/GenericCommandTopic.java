package io.github.disbatch.command;

import io.github.disbatch.command.exception.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO make valid command usage mechanism possible
/**
 * In the absence of an alternative, a {@link CommandDescriptor} will create a new instance of this to be registered
 * in the server's {@link CommandMap}. You can use this as a base class for custom {@code CommandTopic}s or as an example
 * of how to write your own.
 *
 * @implSpec This internally uses a {@link GenericCommandHelpTopic} instance to fetch all necessary formatted help contents.
 *
 * @since 1.1.0
 */
public class GenericCommandTopic<S extends CommandSender> implements CommandTopic<S> {
    private final String description;
//    private final CommandSyntaxMessage usage;
    private GenericCommandHelpTopic bukkitTopic;

//    public GenericCommandTopic(final @NotNull String description) {
//        this(description, new ParameterUsage.Builder().labelHead('<').labelTail('>').build());
//    }

    /**
     * @param description
     */
    public GenericCommandTopic(final @NotNull String description) {
        this.description = description;
//        this.usage = usage;
    }

    @Override
    public void amend(final @Nullable String shortText, final @Nullable String fullText) {
        throwIfNotApplied();
        bukkitTopic.amendTopic(shortText, fullText);
    }

    @Override
    public void apply(final CommandDescriptor descriptor) {
//        final org.bukkit.command.Command placeholder = new PlaceholderCommand(
//                descriptor.getLabel(),
//                description,
//                descriptor.getAliases(),
//                descriptor.getSyntax()
//        );
//        bukkitTopic = new GenericCommandHelpTopic(placeholder);
    }

    @Override
    public boolean isViewableTo(final CommandSender sender) {
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

//    private class PlaceholderCommand extends org.bukkit.command.Command {
//        private PlaceholderCommand(final String label, final String description, final List<String> aliases) {
//            super(
//                label,
//                description,
//                "/<command>" + (commandSyntaxLiteral.isValid() ? usage.createUsageString(commandSyntaxLiteral) : StringUtils.EMPTY),
//                aliases
//            );
//        }
//
//        @Override
//        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
//            return false;
//        }
//    }
}
