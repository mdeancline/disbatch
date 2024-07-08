package io.github.disbatch;

import io.github.disbatch.command.TabCompleter;
import io.github.disbatch.command.TabCompleters;
import io.github.disbatch.mock.DummyCommandLine;
import org.bukkit.command.CommandSender;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class TabCompleterTest extends EasyMockSupport {
    private CommandSender senderMock;

    @Before
    public void setup() {
        senderMock = createMock(CommandSender.class);
    }

    @Test
    public void testForFirstArgumentSuggester() {
        final TabCompleter<CommandSender> tabCompleter = TabCompleters.forFirstArgument(TabCompleters.of("1", "2"));

        System.out.println("Suggestions: " + tabCompleter.tabComplete(senderMock, new DummyCommandLine("test")));
        System.out.println("Empty suggestions: " + tabCompleter.tabComplete(senderMock, new DummyCommandLine("test 1")));
    }
}
