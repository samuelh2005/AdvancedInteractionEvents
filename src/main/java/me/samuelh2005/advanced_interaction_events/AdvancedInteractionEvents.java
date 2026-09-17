package me.samuelh2005.advanced_interaction_events;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.samuelh2005.advanced_interaction_events.builtin.BuiltinEventHandlers;

public class AdvancedInteractionEvents implements ModInitializer {
    public static final String MOD_ID = "advanced_interaction_events";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BuiltinEventHandlers.register();
        LOGGER.info("Advanced Interaction Events initialised.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
