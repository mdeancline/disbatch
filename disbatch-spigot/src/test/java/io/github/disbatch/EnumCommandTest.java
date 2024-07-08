package io.github.disbatch;

import io.github.disbatch.command.parameter.ParameterUsage;
import io.github.disbatch.command.parameter.ParameterUsages;
import io.github.disbatch.command.parameter.ParameterizedCommand;
import io.github.disbatch.mock.DummyCommandLine;
import io.github.disbatch.mock.EntityTypeCaseInsensitiveCommand;
import io.github.disbatch.mock.EntityTypeCaseSensitiveCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class EnumCommandTest extends EasyMockSupport {
    private ParameterizedCommand<CommandSender, EntityType> caseInsensitiveCmd;
    private ParameterizedCommand<CommandSender, EntityType> caseSensitiveCmd;
    private CommandSender senderMock;

    @Before
    public void setup() {
        senderMock = createMock(CommandSender.class);

        final ParameterUsage usage = ParameterUsages.withChevrons("Usage: %usage");

        caseInsensitiveCmd = new EntityTypeCaseInsensitiveCommand(usage);
        caseSensitiveCmd = new EntityTypeCaseSensitiveCommand(usage);
    }

    @Test
    public void testEnumCommandExecution() {
        final String entityTypeName = EntityType.SHEEP.name();

        caseInsensitiveCmd.execute(senderMock, new DummyCommandLine(entityTypeName));
        caseSensitiveCmd.execute(senderMock, new DummyCommandLine(entityTypeName));
    }

    @Test
    public void testEnumCommandTabCompletion() {
        caseInsensitiveCmd.tabComplete(senderMock, new DummyCommandLine(EntityType.SHEEP.name()));
        caseSensitiveCmd.tabComplete(senderMock, new DummyCommandLine(EntityType.SHEEP.name()));
    }
}
