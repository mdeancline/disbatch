package io.github.disbatch;

import io.github.disbatch.command.parameter.Suggester;
import io.github.disbatch.command.parameter.Suggesters;
import io.github.disbatch.mock.DummyCommandLine;
import org.bukkit.command.CommandSender;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class SuggesterTest extends EasyMockSupport {
    private CommandSender senderMock;

    @Before
    public void setup() {
        senderMock = createMock(CommandSender.class);
    }

    @Test
    public void testForFirstArgumentSuggester() {
        final Suggester<CommandSender> suggester = Suggesters.forFirstArgument(Suggesters.of("1", "2"));

        System.out.println("Suggestions: " + suggester.getSuggestions(senderMock, new DummyCommandLine("test")));
        System.out.println("Empty suggestions: " + suggester.getSuggestions(senderMock, new DummyCommandLine("test 1")));
    }
}
