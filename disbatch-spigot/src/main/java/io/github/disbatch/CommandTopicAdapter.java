package io.github.disbatch;

import io.github.disbatch.command.descriptor.CommandTopic;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

class CommandTopicAdapter extends HelpTopic {
    protected final CommandTopic adaptedTopic;

    CommandTopicAdapter(final String name, final CommandTopic adaptedTopic) {
        this.adaptedTopic = adaptedTopic;
        this.name = "/" + name;
    }

    @Override
    public boolean canSee(final CommandSender sender) {
        return adaptedTopic.canSee(sender);
    }

    @Override
    public String getShortText() {
        return adaptedTopic.getShortText();
    }

    @Override
    public String getFullText(final CommandSender forWho) {
        return adaptedTopic.getFullText(forWho);
    }

    @Override
    public void amendTopic(final String amendedShortText, final String amendedFullText) {
        shortText = adaptedTopic.applyAmendment(shortText, amendedShortText);
        fullText = adaptedTopic.applyAmendment(fullText, amendedFullText);
    }
}
