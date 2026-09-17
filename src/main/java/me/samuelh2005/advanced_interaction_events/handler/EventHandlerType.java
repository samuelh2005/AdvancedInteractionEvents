package me.samuelh2005.advanced_interaction_events.handler;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.event.EventData;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class EventHandlerType<T extends EventData> {
    public abstract MapCodec<T> codec();

    public static final Registry<EventHandlerType<?>> REGISTRY = new MappedRegistry<>(ResourceKey.createRegistryKey(
        AdvancedInteractionEvents.id("event_handlers")),
        Lifecycle.stable()
    );
}
