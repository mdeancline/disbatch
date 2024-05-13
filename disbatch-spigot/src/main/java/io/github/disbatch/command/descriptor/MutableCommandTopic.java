package io.github.disbatch.command.descriptor;

import org.bukkit.command.CommandSender;

class MutableCommandTopic implements CommandTopic {
    private CommandTopic innerTopic;

    MutableCommandTopic(final CommandTopic innerTopic) {
        this.innerTopic = innerTopic;
    }

    @Override
    public String applyAmendment(final String baseText, final String amendment) {
        return innerTopic.applyAmendment(baseText, amendment);
    }

    @Override
    public boolean canSee(final CommandSender forWho) {
        return innerTopic.canSee(forWho);
    }

    @Override
    public String getShortText() {
        return innerTopic.getShortText();
    }

    @Override
    public String getFullText(final CommandSender forWho) {
        return innerTopic.getFullText(forWho);
    }

    void setTopic(final CommandTopic innerTopic) {
        this.innerTopic = innerTopic;
    }
}
