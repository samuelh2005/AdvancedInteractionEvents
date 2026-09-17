package me.samuelh2005.advanced_interaction_events.event;

import java.util.Optional;

import com.mojang.serialization.DataResult;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.handler.EventHandlerType;
import me.samuelh2005.advanced_interaction_events.network.UserEventPayload;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.dialog.action.StaticAction;

public class EventDataEncoders {
    public static <E extends EventData, T extends EventHandlerType<E>> Optional<Tag> encodeNetwork(T type, E data) {
        Identifier id = EventHandlerType.REGISTRY.getKey(type);
        UserEventPayload payload = new UserEventPayload(id, data);
        DataResult<Tag> result = UserEventPayload.CODEC.encodeStart(NbtOps.INSTANCE, payload);
        Optional<Tag> nbt = result.resultOrPartial(AdvancedInteractionEvents.LOGGER::error);
        return nbt;
    }

    public static <E extends EventData, T extends EventHandlerType<E>> ClickEvent.Custom encodeClickEvent(T type, E data) {
        Optional<Tag> nbt = encodeNetwork(type, data);
        return new ClickEvent.Custom(UserEventPayload.USER_EVENT, nbt);
    }

    public static <E extends EventData, T extends EventHandlerType<E>> StaticAction encodeStaticAction(T type, E data) {
        ClickEvent.Custom clickEvent = encodeClickEvent(type, data);
        return new StaticAction(clickEvent);
    }
}
