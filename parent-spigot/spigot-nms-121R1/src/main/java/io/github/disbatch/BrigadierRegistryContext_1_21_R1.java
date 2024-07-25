package io.github.disbatch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;

class BrigadierRegistryContext_1_21_R1 implements BrigadierRegistryContext {
    @Override
    public BrigadierRegistry getRegistry() {
        return new BrigadierRegistryImpl(((CraftServer) Bukkit.getServer()).getServer().aH().a());
    }

    private record BrigadierRegistryImpl(
            CommandDispatcher<CommandListenerWrapper> source) implements BrigadierRegistry {
        @Override
        public void register(final LiteralArgumentBuilder<CommandSender> builder) {
            final String literal = builder.getLiteral();
            source.register(LiteralArgumentBuilder.<CommandListenerWrapper>literal(literal)
                    .executes(new CommandWrapper(builder.getCommand())));
        }
    }

    private record CommandWrapper(Command<CommandSender> source) implements Command<CommandListenerWrapper> {
        @Override
        public int run(final CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
            final CommandListenerWrapper wrapper = context.getSource();
            final CommandSender sender = wrapper.getBukkitSender();

            return source.run(new CommandContextBuilder<>(null, sender, null, 0).build(context.getInput()));
        }
    }
}
