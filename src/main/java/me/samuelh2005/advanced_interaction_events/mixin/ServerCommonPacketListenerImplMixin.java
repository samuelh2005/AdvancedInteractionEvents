package me.samuelh2005.advanced_interaction_events.mixin;

import net.minecraft.core.Holder.Reference;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.events.ClickActionHandler;
import me.samuelh2005.advanced_interaction_events.handler.EventData;
import me.samuelh2005.advanced_interaction_events.handler.EventHandlerType;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
    public static final Identifier CUSTOM_CLICK_EVENT = AdvancedInteractionEvents.id("custom_click_event");

    public static record CustomClickActionPayload(Identifier id, EventData payload) {
        public static final Codec<CustomClickActionPayload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(CustomClickActionPayload::id),
            EventData.CODEC.fieldOf("payload").forGetter(CustomClickActionPayload::payload)
        ).apply(instance, CustomClickActionPayload::new));
    }

    @Inject(method = "handleCustomClickAction", at = @At("TAIL"))
    private void onCustomClickAction(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if (packet.id() == CUSTOM_CLICK_EVENT || !(this instanceof ServerPlayerConnection connection)) {
            return;
        }

        Optional<Tag> payload = packet.payload();
        ServerPlayer player = connection.getPlayer();
        MinecraftServer server = ((ServerCommonPacketListenerImplAccessor) this).advanced_interaction_events$getServer();

        if (payload.isPresent()) {
            Tag tag = payload.get();
            DataResult<CustomClickActionPayload> result = CustomClickActionPayload.CODEC.parse(NbtOps.INSTANCE, tag);

            Optional<CustomClickActionPayload> optionalPayload = result.resultOrPartial(AdvancedInteractionEvents.LOGGER::error);
            optionalPayload.ifPresent(customClickActionPayload -> {
                Identifier id = customClickActionPayload.id();
                EventData eventData = customClickActionPayload.payload();

                Optional<Reference<EventHandlerType<?>>> eventHandlerTypeRef = EventHandlerType.REGISTRY.get(id);
                if (eventHandlerTypeRef.isPresent()) {
                    EventHandlerType<? extends EventData> eventHandlerType = eventHandlerTypeRef.get().value();
                    if (eventData.getType() != eventHandlerType) {
                        AdvancedInteractionEvents.LOGGER.error("Received custom click action packet with event type that does not match the expected type: {} from player: {}", id, player.getName().getString());
                        return;
                    }

                    if (eventHandlerType instanceof ClickActionHandler<?> clickActionHandler) {
                        handleClickAction(clickActionHandler, server, player, eventData);
                    } else {
                        AdvancedInteractionEvents.LOGGER.error("Received custom click action packet with event type that does not implement ClickActionHandler: {} from player: {}", id, player.getName().getString());
                    }
                } else {
                    AdvancedInteractionEvents.LOGGER.error("Received custom click action packet with unknown event type: {} from player: {}", id, player.getName().getString());
                }
            });
        } else {
            AdvancedInteractionEvents.LOGGER.error("Received custom click action packet with no payload from player: {}", player.getName().getString());
        }
    }

    @SuppressWarnings("unchecked") // Safety: We check that the eventData type matches the clickActionHandler type before calling handleClickAction.
    private static <T extends EventData> void handleClickAction(ClickActionHandler<T> clickActionHandler,
            MinecraftServer server, ServerPlayer player, EventData eventData) {
        clickActionHandler.handleClickAction(server, player, (T) eventData);
    }
}