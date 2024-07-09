package io.github.disbatch;

import com.google.common.collect.ImmutableList;
import io.github.disbatch.command.Command;
import io.github.disbatch.command.CommandGroup;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputs;
import io.github.disbatch.command.parameter.ParameterUsage;
import io.github.disbatch.mock.DummyCommandLine;
import org.bukkit.command.CommandSender;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class CommandGroupTest extends EasyMockSupport {
    private CommandGroup<CommandSender> cmdGroup;
    private CommandSender senderMock;

    @Before
    public void setup() {
        senderMock = createMock(CommandSender.class);

        cmdGroup = new CommandGroup<>(new ParameterUsage.Builder()
                .baseMessage("Usage: %usage")
                .labelHead('<')
                .labelTail('>').build());

        senderMock.sendMessage("");
        EasyMock.expectLastCall().andAnswer(() -> {
            System.out.println("Sent to CommandSender: " + EasyMock.getCurrentArguments()[0]);
            return null;
        });

        final Command<CommandSender> cmd = new Command<CommandSender>() {
            @Override
            public void execute(final CommandSender sender, final @NotNull CommandInput input) {
                System.out.printf("Arguments for %s: %s%n", input.getCommandLabel(), input.getArgumentLine());
            }

            @Override
            public List<String> tabComplete(final CommandSender sender, final @NotNull CommandInput input) {
                return ImmutableList.of();
            }
        };

        cmdGroup.withCommand(cmd, "cmd-1")
                .withCommand(cmd, "cmd-2");
    }

    @Test
    public void testCommandGroupTabCompletion() {
        final Collection<String> tabCompletion = cmdGroup.tabComplete(senderMock, new DummyCommandLine("cmd-3"));
        final Collection<String> emptyCompletion = cmdGroup.tabComplete(senderMock, CommandInputs.empty());

        System.out.println("Tab completion: " + tabCompletion);
        System.out.println("Empty tab completion: " + emptyCompletion);
    }

    @Test
    public void testGroupedCommandInput() {
        cmdGroup.execute(senderMock, new SimpleCommandRegistrar.SingleLabelCommandInput("cmd"));
    }
}
