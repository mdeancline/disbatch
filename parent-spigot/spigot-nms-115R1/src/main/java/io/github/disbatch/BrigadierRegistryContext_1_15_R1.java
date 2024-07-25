package io.github.disbatch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

class BrigadierRegistryContext_1_15_R1 implements BrigadierRegistryContext {
    @Override
    public BrigadierRegistry getRegistry() {
        return new BrigadierRegistryImpl(((CraftServer) Bukkit.getServer()).getServer().getCommandDispatcher().a());
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BrigadierRegistryImpl implements BrigadierRegistry {
        private final CommandDispatcher<CommandListenerWrapper> source;

        @Override
        public void register(final LiteralArgumentBuilder<CommandSender> builder) {
            final String literal = builder.getLiteral();
            source.register(LiteralArgumentBuilder.<CommandListenerWrapper>literal(literal)
                    .executes(new CommandWrapper(builder.getCommand())));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CommandWrapper implements Command<CommandListenerWrapper> {
        private final Command<CommandSender> source;

        @Override
        public int run(final CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
            final CommandListenerWrapper wrapper = context.getSource();
            final CommandSender sender = wrapper.getBukkitSender();

            return source.run(new CommandContextBuilder<>(null, sender, null, 0).build(context.getInput()));
        }
    }
}
