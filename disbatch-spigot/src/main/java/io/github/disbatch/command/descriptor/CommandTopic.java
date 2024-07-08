package io.github.disbatch.command.descriptor;

import io.github.disbatch.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Holds information about a {@link Command} that should be displayed to a {@link CommandSender} when executing Spigot's
 * {@code /help} command.
 *
 * @apiNote This simplifies and offers more flexibility for commands than {@link org.bukkit.help.HelpTopic}, such as not
 * having to define a topic name and accounting for multiple {@link CommandSender} types.
 *
 * @since 1.0.0
 */
public interface CommandTopic {

    /**
     * Determines if a {@code CommandSender} is allowed to see this command topic.
     *
     * @param forWho the sender in question
     * @return if the sender can see this command topic, false otherwise
     */
    boolean canSee(CommandSender forWho);

    /**
     * Returns a brief description that will be displayed in the topic index.
     *
     * @return a brief description
     */
    String getShortText();

    /**
     * Returns the full description of this command topic that is displayed when a {@link CommandSender} requests its
     * details. The {@code /help} command will paginate the result to suit their client.
     *
     * @param forWho the sender requesting the full text, which can be helpful for security trimming the result
     *               based on sub-permissions in custom implementations.
     * @return a full description
     */
    String getFullText(CommandSender forWho);

    /**
     * Used to ensure implementation-specific compliance when an external source, such as a {@link Plugin}, edits
     * this command topic's contents.
     *
     * @param baseText  the existing text of the command topic
     * @param amendment the amending text
     * @return the application of the amending text to the existing text
     */
    String applyAmendment(String baseText, String amendment);
}
