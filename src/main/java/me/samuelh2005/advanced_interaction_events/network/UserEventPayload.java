package me.samuelh2005.advanced_interaction_events.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.event.EventData;
import net.minecraft.resources.Identifier;

public record UserEventPayload(Identifier id, EventData payload) {
    public static final Identifier USER_EVENT = AdvancedInteractionEvents.id("user_event");

    public static final Codec<UserEventPayload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("id").forGetter(UserEventPayload::id),
        EventData.CODEC.fieldOf("payload").forGetter(UserEventPayload::payload)
    ).apply(instance, UserEventPayload::new));
}
