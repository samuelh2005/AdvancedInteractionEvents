package me.samuelh2005.advanced_interaction_events.events;

import me.samuelh2005.advanced_interaction_events.handler.EventData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface ClickActionHandler<T extends EventData> {
    void handleClickAction(MinecraftServer server, ServerPlayer player, T eventData);
}
