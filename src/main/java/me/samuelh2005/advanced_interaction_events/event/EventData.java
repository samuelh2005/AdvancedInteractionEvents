package me.samuelh2005.advanced_interaction_events.event;

import com.mojang.serialization.Codec;

import me.samuelh2005.advanced_interaction_events.handler.EventHandlerType;

public interface EventData {
    Codec<EventData> CODEC = EventHandlerType.REGISTRY.byNameCodec()
        .dispatch("type", EventData::getType, EventHandlerType::codec);

    EventHandlerType<? extends EventData> getType();
}
