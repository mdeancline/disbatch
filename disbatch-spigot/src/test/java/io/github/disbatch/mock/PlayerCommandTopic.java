package io.github.disbatch.mock;

import io.github.disbatch.command.descriptor.CommandTopic;
import org.bukkit.command.CommandSender;

public class PlayerCommandTopic implements CommandTopic {
    @Override
    public boolean canSee(final CommandSender forWho) {
        return true;
    }

    @Override
    public String getShortText() {
        return "This is the short text for " + getClass().getSimpleName();
    }

    @Override
    public String getFullText(final CommandSender forWho) {
        return "This is the full text for " + getClass().getSimpleName();
    }

    @Override
    public String applyAmendment(final String baseText, final String amendment) {
        return baseText;
    }
}
