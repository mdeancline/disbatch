package io.github.disbatch;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.disbatch.command.CommandExecutor;
import io.github.disbatch.command.CommandInput;
import io.github.disbatch.command.CommandInputs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

public class CommandRegistrationTest {
    private CommandDispatcher<CommandSender> dispatcher;
    private BrigadierCommandRegistrar mockBrigadierRegistrar;
    private BukkitCommandRegistrar mockBukkitRegistrar;
    private ServerMock mockServer;

    @Before
    public void setup() {
        mockServer = MockBukkit.getOrCreateMock();
        dispatcher = new CommandDispatcher<>();
        mockBrigadierRegistrar = new BrigadierCommandRegistrar(new MockBrigadierRegistryContext(dispatcher));
        mockBukkitRegistrar = new BukkitCommandRegistrar();
    }

    @Test
    public void testBrigadierRegistrarRegistration() throws CommandSyntaxException {
        final PlayerMock mockPlayer = mockServer.addPlayer(TestUtils.randomString());
        mockBrigadierRegistrar.register(createMockCommand());
        dispatcher.execute(createCommandLine(), mockPlayer);
    }

    @Test
    public void testBukkitRegistrarRegistration() {
        final PlayerMock mockPlayer = mockServer.addPlayer(TestUtils.randomString());
        mockBukkitRegistrar.register(createMockCommand());
        mockServer.dispatchCommand(mockPlayer, createCommandLine());
    }

    private Command createMockCommand() {
        final CommandExecutor<Player, CommandInput> executor = (sender, input) ->
                sender.sendMessage(sender.getName() + " passed: " + input.getArgumentLine());
        return Command.builder(Player.class, executor).label("test").syntax(CommandInputs.syntax()).build();
    }

    private String createCommandLine() {
        return "test this is a test message";
    }
}
