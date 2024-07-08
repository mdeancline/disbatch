package io.github.disbatch.command.descriptor;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @since 1.0.0
 */
public final class GenericCommandTopic implements CommandTopic {
    private static final String LABEL_PLACEHOLDER = "%label";
    private static final String ALIASES_PLACEHOLDER = "%aliases";

    private final String label;
    private final String[] aliases;
    private final HelpTopic internalTopic;

    /**
     * @param description
     */
    public GenericCommandTopic(final String description) {
        this(new GenericCommandHelpTopic(new PlaceholderCommand(description)), StringUtils.EMPTY, ImmutableList.of());
    }

    GenericCommandTopic(final HelpTopic internalTopic, final String label, final List<String> aliases) {
        this.internalTopic = internalTopic;
        this.label = label;
        this.aliases = aliases.toArray(new String[0]);
    }

    @Override
    public boolean canSee(final CommandSender forWho) {
        return true;
    }

    @Override
    public String getShortText() {
        return internalTopic.getShortText();
    }

    @Override
    public String getFullText(final CommandSender forWho) {
        return internalTopic.getFullText(forWho)
                .replace(LABEL_PLACEHOLDER, label)
                .replace(ALIASES_PLACEHOLDER, String.join(", ", aliases));
    }

    @Override
    public String applyAmendment(final String baseText, final String amendment) {
        return baseText;
    }

    private static class PlaceholderCommand extends Command {
        public PlaceholderCommand(final String description) {
            super(LABEL_PLACEHOLDER, description, "/", Collections.singletonList(ALIASES_PLACEHOLDER));
        }

        @Override
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
            return false;
        }
    }

    static class Finalizer implements CommandTopicFinalizer<GenericCommandTopic> {
        @Override
        public CommandTopic finalize(final GenericCommandTopic topic, final CommandDescriptor descriptor) {
            return new GenericCommandTopic(topic.internalTopic, descriptor.getLabel(), descriptor.getAliases());
        }
    }
}
