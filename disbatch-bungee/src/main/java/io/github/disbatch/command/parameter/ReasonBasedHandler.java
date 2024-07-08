package io.github.disbatch.command.parameter;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * @param <S>
 *
 * @since 1.0.0
 */
public final class ReasonBasedHandler<S extends CommandSender> implements InvalidInputHandler<S> {
    private final Map<InvalidInput.Reason, InvalidInputHandler<S>> handlers = new EnumMap<>(InvalidInput.Reason.class);
    private final InvalidInputHandler<S> fallback;

    /**
     * @param fallback
     */
    public ReasonBasedHandler(final @NotNull InvalidInputHandler<S> fallback) {
        this.fallback = fallback;
    }

    /**
     * @param reason
     * @param handler
     * @return
     */
    public ReasonBasedHandler<S> with(final @NotNull InvalidInput.Reason reason, final @NotNull InvalidInputHandler<S> handler) {
        handlers.put(reason, handler);
        return this;
    }

    @Override
    public void handle(final S sender, final InvalidInput input) {
        handlers.getOrDefault(input.getReason(), fallback).handle(sender, input);
    }
}
