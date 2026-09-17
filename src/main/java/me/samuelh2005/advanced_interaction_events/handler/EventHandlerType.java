package me.samuelh2005.advanced_interaction_events.handler;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class EventHandlerType<T extends EventData> {
    public abstract MapCodec<T> codec();

    public static final Registry<EventHandlerType<?>> REGISTRY = new MappedRegistry<>(ResourceKey.createRegistryKey(
        AdvancedInteractionEvents.id("event_handlers")),
        Lifecycle.stable()
    );

    /**
     * Casts the given event data to the type associated with this EventHandlerType.
     *
     * @param eventData the event data to cast
     * @return the casted event data
     * @throws IllegalArgumentException if the event data is not of the expected type
     */
    @SuppressWarnings({ "unchecked", "hiding" }) // Safety: The cast is safe because the EventHandlerType is associated with the EventData class.
    public <T extends EventData> T cast(EventData eventData) {
        if (!eventData.getType().equals(this)) {
            throw new IllegalArgumentException("Event data type mismatch: expected " + this + ", got " + eventData.getType());
        }
        return (T) eventData;
    }
}
