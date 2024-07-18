package io.github.disbatch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.disbatch.command.CommandExecutor;
import io.github.disbatch.command.descriptor.CommandDescriptor;
import io.github.disbatch.command.syntax.model.PlayerFromNameSyntax;
import io.github.disbatch.mock.MockCommandRegistrar;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class CommandDispatchTest {
    private CommandDispatcher<CommandSender> mockDispatcher;
    private CommandRegistrar mockRegistrar;
    private ServerMock mockServer;
    private ConsoleCommandSender mockConsole;

    @Before
    public void setup() {
        mockServer = MockBukkit.getOrCreateMock();
        mockDispatcher = new CommandDispatcher<>();
        mockRegistrar = new MockCommandRegistrar(mockDispatcher);
        mockConsole = Objects.requireNonNull(mockServer).getConsoleSender();
    }

    @Test
    public void testWithExecutor() throws CommandSyntaxException {
        mockServer.addPlayer("HelloWorld");

        final CommandExecutor<CommandSender, Player> messageExecutor = (sender, input, value) -> value.sendMessage(sender.getName() + " said hi!");
        mockRegistrar.register(CommandDescriptor.of(messageExecutor)
                .syntax(new PlayerFromNameSyntax("name"))
                .label("message")
                .build());

        final int status = mockDispatcher.execute("message HelloWorld", mockConsole);
        System.out.println("Execution dispatch success: " + isSuccessful(status));
    }

    private boolean isSuccessful(final int status) {
        return status == Command.SINGLE_SUCCESS;
    }
}
