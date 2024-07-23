package io.github.disbatch.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

/**
 * Holds information about a {@link Command} that should be displayed to a {@link CommandSender} when executing Spigot's
 * {@code /help} command.
 *
 * @apiNote This simplifies and offers more flexibility for commands than {@link org.bukkit.help.HelpTopic}, such as not
 * having to define a topic name and accounting for multiple {@link CommandSender} types.
 * @since 1.1.0
 */
public interface CommandTopic<S extends CommandSender> {

    /**
     * Allows a server operator or another {@link Plugin} to add or replace the
     * {@code CommandTopic}'s contents.
     * <p>
     * A {@code null} in either parameter will leave that part of the topic unchanged.
     * In either amending parameter, the string {@literal <text>} is replaced
     * with the existing contents in the help topic. Use this to append or
     * prepend additional content into an automatically generated help topic.
     *
     * @param amendedShortText The new topic short text to use, or null to
     *                         leave alone.
     * @param amendedFullText  The new topic full text to use, or null to leave
     *                         alone.
     */
    void amend(@Nullable String shortText, @Nullable String fullText);

    void apply(CommandRegistration registration);

    /**
     * Determines if a {@link CommandSender} is allowed to see this {@code CommandTopic}.
     *
     * @param sender the sender in question
     * @return if the sender can see this command topic, false otherwise
     */
    boolean isViewableTo(S sender);

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
     * @param forSender the sender requesting the full text, which can be helpful for security trimming the result
     *                  based on sub-permissions in custom implementations.
     * @return a full description
     */
    String getFullText(S forSender);
}
