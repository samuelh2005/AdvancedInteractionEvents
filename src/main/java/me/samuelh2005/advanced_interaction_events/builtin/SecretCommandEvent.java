package me.samuelh2005.advanced_interaction_events.builtin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.events.ClickActionHandler;
import me.samuelh2005.advanced_interaction_events.handler.EventData;
import me.samuelh2005.advanced_interaction_events.handler.EventHandlerType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class SecretCommandEvent extends EventHandlerType<SecretCommandEvent.SecretCommandData> implements ClickActionHandler<SecretCommandEvent.SecretCommandData> {
    public static final MapCodec<SecretCommandData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("command").forGetter(SecretCommandData::command)
    ).apply(instance, SecretCommandData::new));

    public static record SecretCommandData(String command) implements EventData {
        @Override
        public EventHandlerType<? extends EventData> getType() {
            return BuiltinEventHandlers.SECRET_COMMAND_EVENT;
        }
    }

    @Override
    public MapCodec<SecretCommandData> codec() {
        return CODEC;
    }

    @Override
    public void handleClickAction(MinecraftServer server, ServerPlayer player, SecretCommandData eventData) {
        try {
            String rawCommand = eventData.command();
            AdvancedInteractionEvents.LOGGER.info("Player \""+player.getName().getString()+"\" requested secret command /"+rawCommand);
            server.getCommands().getDispatcher().execute(rawCommand, player.createCommandSourceStack());
        } catch (CommandSyntaxException e) {
            player.sendSystemMessage(getErrorMessage(e));
            AdvancedInteractionEvents.LOGGER.warn("Exception whilst handling secret command", e);
        }
    }

    // See CommandSuggestions.getExceptionMessage. That cannot be used directly as it returns an FormattedCharSequence instead of a Component.
    private static Component getErrorMessage(CommandSyntaxException e) {
        Component message = ComponentUtils.fromMessage(e.getRawMessage());
        String context = e.getContext();

        return context != null ? Component.translatable("command.context.parse_error", message, e.getCursor(), context) : message;
    }
}
