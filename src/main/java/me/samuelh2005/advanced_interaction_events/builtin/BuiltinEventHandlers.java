package me.samuelh2005.advanced_interaction_events.builtin;

import me.samuelh2005.advanced_interaction_events.AdvancedInteractionEvents;
import me.samuelh2005.advanced_interaction_events.handler.EventData;
import me.samuelh2005.advanced_interaction_events.handler.EventHandlerType;
import net.minecraft.core.Registry;

public class BuiltinEventHandlers {
    public static SecretCommandHandler SECRET_COMMAND = register("secret_command", new SecretCommandHandler());

    private static <T extends EventHandlerType<? extends EventData>> T register(String name, T eventHandlerType) {
        return Registry.register(EventHandlerType.REGISTRY, AdvancedInteractionEvents.id(name), eventHandlerType);
    }

    public static void register() {
        // Do nothing apart from initialise statics.
    }
}
